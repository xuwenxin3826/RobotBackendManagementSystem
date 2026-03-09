package com.ruoyi.taskmgt.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.taskmgt.common.constants.TaskLogEventType;
import com.ruoyi.taskmgt.domain.StepRepository;
import com.ruoyi.taskmgt.domain.TaskRepository;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.domain.bo.TaskStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class TaskTriggerService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StepRepository stepRepository;

    @Autowired
    private TaskLogReuseService taskLogService;

    @Autowired
    private RedisCache redisUtil;

    // 机器人服务接口（待实现）
    // @Autowired
    // private IRobotService robotService;

    /**
     * 每分钟执行一次触发检查
     */
    @Scheduled(cron = "0 * * * * ?")
    public void checkTriggers() {
        log.debug("开始检查任务触发条件");
        checkScheduledTasks();
        checkBatteryTasks();
        checkIdleTasks();
    }

    /**
     * 检查定时任务触发条件
     */
    private void checkScheduledTasks() {
        List<Task> tasks = taskRepository.getTasks(Task.NOTSTART, null, null, null, null, 1, null, null);
        Date now = new Date();
        for (Task task : tasks) {
            if (task.getScheduledTime() != null && !task.getScheduledTime().after(now)) {
                triggerTask(task);
            }
        }
    }

    /**
     * 检查电量任务触发条件
     */
    private void checkBatteryTasks() {
        List<Task> tasks = taskRepository.getTasks(Task.NOTSTART, null, null, null, null, 2, null, null);
        for (Task task : tasks) {
            // Integer battery = robotService.getBatteryLevel(task.getRobotId());
            Integer battery = getMockBattery(task.getRobotId());
            if (battery != null && battery >= task.getBatteryThreshold()) {
                triggerTask(task);
            }
        }
    }

    /**
     * 检查闲时任务触发条件
     */
    private void checkIdleTasks() {
        List<Task> tasks = taskRepository.getTasks(Task.NOTSTART, null, null, null, null, 3, null, null);
        for (Task task : tasks) {
            // String status = robotService.getRobotStatus(task.getRobotId());
            // Date idleSince = robotService.getIdleSince(task.getRobotId());
            String status = getMockRobotStatus(task.getRobotId());
            Date idleSince = getMockIdleSince(task.getRobotId());
            if ("idle".equals(status) && idleSince != null) {
                long idleMinutes = (System.currentTimeMillis() - idleSince.getTime()) / (60 * 1000);
                if (idleMinutes >= task.getIdleTime()) {
                    triggerTask(task);
                }
            }
        }
    }

    /**
     * 触发任务：将任务状态从未开始改为准备中，并设置排队顺序
     */
    private void triggerTask(Task task) {
        if (!task.allowTransitStatus(Task.PENDING)) {
            log.warn("任务 {} 当前状态 {} 不允许转为准备中", task.getId(), task.getStatus());
            return;
        }

        // 设置准备队列顺序
        if (task.getIsGroupTask() == 0) {
            List<Task> pendingTasks = taskRepository.getTasks(Task.PENDING, 0, null,
                    task.getRobotId(), null, null, null, null);
            int maxOrder = pendingTasks.stream().mapToInt(Task::getPendingOrder).max().orElse(0);
            task.setPendingOrder(maxOrder + 1);
        } else {
            List<Task> pendingTasks = taskRepository.getTasks(Task.PENDING, 1, null,
                    null, task.getRobotGroupId(), null, null, null);
            int maxOrder = pendingTasks.stream().mapToInt(Task::getPendingOrder).max().orElse(0);
            task.setPendingOrder(maxOrder + 1);
        }

        task.setStatus(Task.PENDING);
        task.setUpdateBy("system");
        List<String> redisKeys = taskRepository.update(task);
        if (redisKeys != null && !redisKeys.isEmpty()) {
            redisUtil.deleteObject(redisKeys);
        }

        taskLogService.record(task.getId(), null, TaskLogEventType.TASK_PENDING,
                "任务达到触发条件，进入准备队列", "system");
        log.info("任务 {} 已触发进入准备队列", task.getId());
    }

    // 机器人状态变更处理
    public void handleRobotStatusChange(Long robotId, String newStatus) {
        log.info("收到机器人状态变更事件：robotId={}, newStatus={}", robotId, newStatus);

        // 处理非组任务
        List<Task> tasks = taskRepository.getTasks(null, 0, null, robotId, null, null, null, null);
        for (Task task : tasks) {
            updateTaskRisk(task, newStatus);
        }

        // 处理组任务：获取机器人所在组
        Long groupId = getMockRobotGroupId(robotId); // 待替换为 robotService.getRobotGroupId(robotId)
        if (groupId != null) {
            // 获取该组下所有组任务
            List<Task> groupTasks = taskRepository.getTasks(null, 1, null, null, groupId, null, null, null);
            // 获取该组所有机器人的状态
            List<Long> robotIds = getMockRobotIdsByGroupId(groupId); // 待替换
            boolean hasAbnormal = false;
            for (Long rid : robotIds) {
                String status = getMockRobotStatus(rid);
                if ("offline".equals(status) || "fault".equals(status) || "low_battery".equals(status)) {
                    hasAbnormal = true;
                    break;
                }
            }
            for (Task task : groupTasks) {
                if (hasAbnormal) {
                    int riskLevel = (task.getStatus() == Task.EXECUTING) ? 2 : 1;
                    task.setRiskLevel(riskLevel);
                    taskLogService.record(task.getId(), null, TaskLogEventType.ROBOT_STATUS_CHANGE,
                            String.format("组内机器人状态异常，任务标记为%s", riskLevel == 2 ? "高风险" : "风险"), "system");
                } else {
                    task.setRiskLevel(0);
                    taskLogService.record(task.getId(), null, TaskLogEventType.ROBOT_STATUS_CHANGE,
                            "组内机器人状态恢复正常，任务风险清除", "system");
                }
                task.setUpdateBy("system");
                List<String> redisKeys = taskRepository.update(task);
                if (redisKeys != null && !redisKeys.isEmpty()) {
                    redisUtil.deleteObject(redisKeys);
                }
            }
        }
    }

    private void updateTaskRisk(Task task, String newStatus) {
        if (Objects.equals(task.getStatus(), Task.DISABLED) || Objects.equals(task.getStatus(), Task.TERMINATED) ||
                Objects.equals(task.getStatus(), Task.FINISHED) || Objects.equals(task.getStatus(), Task.DELETED)) {
            return;
        }
        if ("offline".equals(newStatus) || "fault".equals(newStatus) || "low_battery".equals(newStatus)) {
            int riskLevel = (Objects.equals(task.getStatus(), Task.EXECUTING)) ? 2 : 1;
            task.setRiskLevel(riskLevel);
            taskLogService.record(task.getId(), null, TaskLogEventType.ROBOT_STATUS_CHANGE,
                    String.format("机器人状态变更为%s，任务标记为%s", newStatus,
                            riskLevel == 2 ? "高风险" : "风险"), "system");
        } else if ("online".equals(newStatus)) {
            // 机器人恢复正常，不清除风险，等待管理员手动解决
        }
        task.setUpdateBy("system");
        List<String> redisKeys = taskRepository.update(task);
        if (redisKeys != null && !redisKeys.isEmpty()) {
            redisUtil.deleteObject(redisKeys);
        }
    }

    //任务开始触发
    @Scheduled(fixedDelay = 10000) // 每10秒检查一次
    public void checkPendingTasksToStart() {
        log.debug("检查准备中的任务是否可以开始执行");

        // 非组任务
        List<Task> pendingNonGroup = taskRepository.getTasks(Task.PENDING, 0, null, null, null, null, null, null);
        Map<Long, Task> minTaskPerRobot = pendingNonGroup.stream()
                .filter(t -> t.getRobotId() != null)
                .collect(Collectors.toMap(
                        Task::getRobotId,
                        t -> t,
                        (t1, t2) -> t1.getPendingOrder() < t2.getPendingOrder() ? t1 : t2
                ));
        for (Task task : minTaskPerRobot.values()) {
            List<Task> executing = taskRepository.getTasks(Task.EXECUTING, 0, null,
                    task.getRobotId(), null, null, null, null);
            if (executing.isEmpty()) {
                startTask(task);
            }
        }

        // 组任务
        List<Task> pendingGroup = taskRepository.getTasks(Task.PENDING, 1, null, null, null, null, null, null);
        Map<Long, Task> minTaskPerGroup = pendingGroup.stream()
                .filter(t -> t.getRobotGroupId() != null)
                .collect(Collectors.toMap(
                        Task::getRobotGroupId,
                        t -> t,
                        (t1, t2) -> t1.getPendingOrder() < t2.getPendingOrder() ? t1 : t2
                ));
        for (Task task : minTaskPerGroup.values()) {
            Long groupId = task.getRobotGroupId();
            // 是否有正在执行的组任务
            List<Task> executingGroup = taskRepository.getTasks(Task.EXECUTING, 1, null,
                    null, groupId, null, null, null);
            if (!executingGroup.isEmpty()) {
                continue;
            }
            // 检查组内机器人是否有正在执行的非组任务
            List<Long> robotIds = getMockRobotIdsByGroupId(groupId);
            boolean hasExecutingNonGroup = false;
            for (Long rid : robotIds) {
                List<Task> executingNonGroup = taskRepository.getTasks(Task.EXECUTING, 0, null,
                        rid, null, null, null, null);
                if (!executingNonGroup.isEmpty()) {
                    hasExecutingNonGroup = true;
                    break;
                }
            }
            if (!hasExecutingNonGroup) {
                startTask(task);
            }
        }
    }

    private void startTask(Task task) {
        if (!task.allowTransitStatus(Task.EXECUTING)) {
            log.warn("任务 {} 无法转为执行中", task.getId());
            return;
        }
        task.setStatus(Task.EXECUTING);
        task.setPendingOrder(null); // 清空准备顺序
        task.setUpdateBy("system");
        List<String> redisKeys = taskRepository.update(task);
        if (redisKeys != null && !redisKeys.isEmpty()) {
            redisUtil.deleteObject(redisKeys);
        }
        taskLogService.record(task.getId(), null, TaskLogEventType.TASK_START,
                "任务开始执行", "system");
        log.info("任务 {} 开始执行", task.getId());

        // 触发第一个步骤（如果有模板）
        if (task.getTemplateId() != null) {
            List<TaskStep> steps = stepRepository.findStepesByTaskId(task.getId());
            steps.stream()
                    .min(Comparator.comparing(TaskStep::getOrderNum))
                    .ifPresent(this::startStep);
        }
    }

    private void startStep(TaskStep step) {
        if (!Objects.equals(step.getStatus(), TaskStep.NOTSTART)) return;
        step.setStatus(TaskStep.EXECUTING);
        step.setStartTime(new Date());
        List<String> redisKeys = stepRepository.update(step);
        if (redisKeys != null && !redisKeys.isEmpty()) {
            redisUtil.deleteObject(redisKeys);
        }
        taskLogService.record(step.getTaskId(), step.getId(), TaskLogEventType.STEP_START,
                "步骤 " + step.getStepName() + " 开始执行", "system");
    }

    // 模拟方法（待替换为真实机器人服务调用）
    private Long getMockRobotGroupId(Long robotId) { return 1L; }
    private List<Long> getMockRobotIdsByGroupId(Long groupId) { return Arrays.asList(1L, 2L, 3L); }
    private String getMockRobotStatus(Long robotId) { return "online"; }

    private Integer getMockBattery(Long robotId) { return 80; }
    private Date getMockIdleSince(Long robotId) { return new Date(System.currentTimeMillis() - 10 * 60 * 1000);}
}
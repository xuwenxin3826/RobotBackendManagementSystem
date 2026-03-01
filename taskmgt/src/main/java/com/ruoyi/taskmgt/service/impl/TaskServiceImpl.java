package com.ruoyi.taskmgt.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ReturnNo;
import com.ruoyi.common.exception.task.TaskmgtException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.taskmgt.domain.StepRepository;
import com.ruoyi.taskmgt.domain.TaskRepository;
import com.ruoyi.taskmgt.domain.TemplateRepository;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.service.StepReuseService;
import com.ruoyi.taskmgt.service.vo.TaskVo;
import com.ruoyi.common.utils.CloneFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class TaskServiceImpl {
    private final TaskRepository taskRepository;
    private final MessageSourceAccessor messageSourceAccessor;
    private final RedisCache redisUtil;
    private final TemplateRepository templateRepository;
    private final StepReuseService stepReuseService;
    private final StepRepository stepRepository;
    public TaskServiceImpl(TaskRepository taskRepository, MessageSourceAccessor messageSourceAccessor, RedisCache redisUtil, TemplateRepository templateRepository, StepReuseService stepReuseService, StepRepository stepRepository) {
        this.taskRepository = taskRepository;
        this.messageSourceAccessor = messageSourceAccessor;
        this.redisUtil = redisUtil;
        this.templateRepository = templateRepository;
        this.stepReuseService = stepReuseService;
        this.stepRepository = stepRepository;
    }

    /**
     * @param task 即将新增的任务
     * @return
     * &#064;description    新增任务
     **/
    public TaskVo createTask(Task task) {
        task.setStatus(Task.NOTSTART);
        Task newTask = this.taskRepository.insert(task);
        TaskVo taskVo = CloneFactory.copy(new TaskVo(), newTask);
        taskVo.setTemplateName(this.templateRepository.getTemplateNameById(task.getTemplateId()));
        return taskVo;
    }

    /**
     * @param task 保存了修改信息的任务
     **/
    public void updateTask(Task task) {
        if(!Objects.equals(task.getStatus(), Task.DISABLED)){
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), task.getId().toString(), task.getStatus().toString()};
            throw new TaskmgtException(ReturnNo.STATENOTALLOW,args,this.messageSourceAccessor.getMessage(ReturnNo.STATENOTALLOW.getMessage()));
        }
        else {
            List<String> redisKeys = this.taskRepository.update(task);
            this.redisUtil.deleteObject(redisKeys);
        }
    }

    /**
     * @param task   任务
     * @param status 需要更改到的状态1
     * @return 修改后任务的redisKey
     * &#064;description   更新任务的状态
     **/
    public List<String> updateTaskStatus(Task task, Byte status) {
        if (Objects.nonNull(task) && task.allowTransitStatus(status)) {
            task.setStatus(status);
            return this.taskRepository.update(task);
        } else {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), task.getId().toString(), task.getStatus().toString()};
            throw new TaskmgtException(ReturnNo.STATENOTALLOW,args,this.messageSourceAccessor.getMessage(ReturnNo.STATENOTALLOW.getMessage()));
        }
    }

    /**
     * 删除任务（被禁用的任务才能删除）
     * @param id 要删除的任务的id
     **/
    public void deleteTask(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });

        List<String> redisKeys = this.updateTaskStatus(task,Task.DELETED);
        Set<String> redisStepKeys = this.stepRepository.deleteStepsByTaskId(id);
        redisKeys.addAll(redisStepKeys);
        this.redisUtil.deleteObject(redisKeys);

    }

    /**
     * 查询任务列表，
     */
    public List<TaskVo> retrieveTasks(Byte status, Integer isGroupTask, String name, Long robotId, Long robotGroupId, Integer taskType, Integer riskLevel, Long templateId) {
        List<Task> tasks = this.taskRepository.getTasks(status, isGroupTask, name, robotId, robotGroupId, taskType, riskLevel, templateId);
        if(StringUtils.isNull(isGroupTask)){
            if (StringUtils.isNotNull(robotId)){
                /*RobotRepository robotRepository;
                robotGroupId=robotRepository.findById(robotId).getRobotGroupId();*/
                tasks.addAll(this.taskRepository.getTasks(status,null,name, null, robotGroupId, taskType, riskLevel, templateId));
            }
            else if(StringUtils.isNotNull(robotGroupId)){
                List<Long> robotIds = new ArrayList<>();
                /*
                RobotRepository robotRepository;
                robotIds = robotRepository.getRobotIdsByRobotGroupId(robotId);
                * */
                tasks.addAll(this.taskRepository.getTasksByRobotIds(status,null,name, robotIds, null, taskType, riskLevel, templateId));
            }
        }
        return tasks.stream()
                .map(task -> {
                    TaskVo taskVo = CloneFactory.copy(new TaskVo(), task);
                    String templateName = this.templateRepository.getTemplateNameById(task.getTemplateId());
                    taskVo.setTemplateName(templateName);
                    return taskVo;
                })
                .collect(Collectors.toList());
    }

    /**
     * 查询任务详情
     * @param id 任务id
     * @return taskVo 任务Vo
     */
    public TaskVo getTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        TaskVo taskVo = CloneFactory.copy(new TaskVo(),task);
        taskVo.setTemplateName(this.templateRepository.getTemplateNameById(task.getTemplateId()));
        return taskVo;
    }

    /**
     * 禁用任务
     * @param id
     */
    public void banTask(Long id){
        Task task = taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        List<String> redisKeys = this.updateTaskStatus(task,Task.DISABLED);
        this.redisUtil.deleteObject(redisKeys);
    }

    /**
     * 恢复任务
     * @param id 被恢复的任务的id
     */
    public void resumeTask(Long id){
        Task task = taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        if(task.getStatus()!=Task.DISABLED){
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), task.getId().toString(), task.getStatus().toString()};
            throw new TaskmgtException(ReturnNo.STATENOTALLOW,args,this.messageSourceAccessor.getMessage(ReturnNo.STATENOTALLOW.getMessage()));
        }
        List<String> redisKeys = this.updateTaskStatus(task,Task.NOTSTART);
        this.redisUtil.deleteObject(redisKeys);
    }

    /**
     * 暂停任务
     * @param id 任务的id
     */
    public void pauseTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        List<String> redisKeys = this.updateTaskStatus(task,Task.PAUSED);
        List<String> stepRedisKeys = this.stepReuseService.pauseStepsByTaskId(id);
        redisKeys.addAll(stepRedisKeys);
        this.redisUtil.deleteObject(redisKeys);
    }

    /**
     * 继续任务
     * @param id 被暂停的任务的id
     */
    public void continueTask(Long id) {
        Task task = this.taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        List<String> redisKeys;
        List<Task>tasks = new ArrayList<>();
        if(task.getIsGroupTask().equals(0)){
            tasks.addAll(this.taskRepository.findByRobotIdAndStatus(task.getRobotId(),Task.EXECUTING));
        }
        else{
            //组任务需确认该组所有的机器人都没有正在执行的任务
            /*
            RobotRepository robotRepository;
            List<Robot> robots = robotRepository.findByRobotGroupId(task.getRobotGroupId());
            for(Robot robot : robots){
                tasks.addAll(this.taskRepository.findByRobotIdAndStatus(robot.getId(),Task.EXECUTING));
            }
            * */
        }
        if (StringUtils.isEmpty(tasks)){
            redisKeys = this.updateTaskStatus(task,Task.EXECUTING);
            List<String> stepRedisKeys = this.stepReuseService.continueStepsByTaskId(id);
            redisKeys.addAll(stepRedisKeys);
        }
        else redisKeys = this.updateTaskStatus(task,Task.PENDING);
        this.redisUtil.deleteObject(redisKeys);
    }

    /**
     * 停止任务
     * @param id 任务的id
     */
    public void terminateTask(Long id,String terminateReason) {
        Task task = taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        task.setTerminateReason(terminateReason);
        List<String> redisKeys = this.updateTaskStatus(task,Task.TERMINATED);
        List<String> stepRedisKeys = this.stepReuseService.terminatedStepsByTaskId(id);
        redisKeys.addAll(stepRedisKeys);
        this.redisUtil.deleteObject(redisKeys);
    }

    /**
     * 取消准备中的任务
     * @param id 准备中任务的id
     */
    public void cancelTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name",LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        List<String> redisKeys = this.updateTaskStatus(task,Task.NOTSTART);
        this.redisUtil.deleteObject(redisKeys);
    }
}

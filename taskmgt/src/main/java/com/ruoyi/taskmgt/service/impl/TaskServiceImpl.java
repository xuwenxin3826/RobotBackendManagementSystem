package com.ruoyi.taskmgt.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ReturnNo;
import com.ruoyi.common.exception.task.TaskmgtException;
import com.ruoyi.taskmgt.domain.TaskRepository;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.service.vo.TaskVo;
import com.ruoyi.common.utils.CloneFactory;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.List;
import java.util.Objects;

public class TaskServiceImpl {
    private final TaskRepository taskRepository;
    private MessageSourceAccessor messageSourceAccessor;
    private final RedisCache redisUtil;
    public TaskServiceImpl(TaskRepository taskRepository, RedisCache redisUtil) {
        this.taskRepository = taskRepository;
        this.redisUtil = redisUtil;
    }

    /**
     * @param task 即将新增的任务
     * @return
     * @description   新增任务
     **/
    public TaskVo createTask(Task task) {
        task.setStatus(Task.NOTSTART);
        Task newTask = this.taskRepository.insert(task);
        return CloneFactory.copy(new TaskVo(), newTask);
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
     * @return
     * @description  更新任务的状态
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
}

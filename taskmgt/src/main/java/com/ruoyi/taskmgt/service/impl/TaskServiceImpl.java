package com.ruoyi.taskmgt.service.impl;

import com.ruoyi.taskmgt.domain.TaskRepository;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.service.vo.TaskVo;
import com.ruoyi.common.utils.CloneFactory;

public class TaskServiceImpl {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskVo createTask(Task task) {

        Task newTask = this.taskRepository.insert(task);
        return CloneFactory.copy(new TaskVo(), newTask);
    }
}

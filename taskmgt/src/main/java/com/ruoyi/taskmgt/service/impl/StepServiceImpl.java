package com.ruoyi.taskmgt.service.impl;

import com.ruoyi.common.enums.ReturnNo;
import com.ruoyi.common.exception.task.TaskmgtException;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.taskmgt.domain.StepRepository;
import com.ruoyi.taskmgt.domain.TaskRepository;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.domain.bo.TaskStep;
import com.ruoyi.taskmgt.service.vo.TaskStepVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StepServiceImpl {
    private final TaskRepository taskRepository;
    private final MessageSourceAccessor messageSourceAccessor;
    private final StepRepository stepRepository;

    public List<TaskStepVo> createSteps(Long taskId,List<TaskStep> steps) {
        Assert.notEmpty(steps, "steps cannot be empty");
        Task task = this.taskRepository.findById(taskId).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Task.name", LocaleContextHolder.getLocale()), taskId.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        List<TaskStep> savedSteps = steps.stream()
                .map(step -> {
                    step.setTaskId(taskId);
                    step.setStatus(TaskStep.NOTSTART);
                    return this.stepRepository.insert(step);
                })
                .collect(Collectors.toList());

        return savedSteps.stream()
                .map(step -> {
                    TaskStepVo vo = CloneFactory.copy(new TaskStepVo(), step);
                    vo.setTaskName(task.getName());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}

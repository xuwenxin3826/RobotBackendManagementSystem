package com.ruoyi.taskmgt.service;

import com.ruoyi.taskmgt.domain.StepRepository;
import com.ruoyi.taskmgt.domain.bo.TaskStep;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StepReuseService {
    private final StepRepository stepRepository ;
    public StepReuseService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    public List<String> pauseStepsByTaskId(Long taskId) {
        List<TaskStep> steps = this.stepRepository.findStepesByTaskId(taskId);
        List<String> redisKeys = new ArrayList<>(List.of());
        for(TaskStep step : steps){
            if(Objects.equals(step.getStatus(), TaskStep.EXECUTING)){
                step.setStatus(TaskStep.PAUSED);
                redisKeys.addAll(this.stepRepository.update(step));
            }
        }
        return redisKeys;
    }

    public List<String> continueStepsByTaskId(Long taskId) {
        List<TaskStep> steps = this.stepRepository.findStepesByTaskId(taskId);
        List<String> redisKeys = new ArrayList<>(List.of());
        for(TaskStep step : steps){
            if(Objects.equals(step.getStatus(), TaskStep.PAUSED)){
                step.setStatus(TaskStep.EXECUTING);
                redisKeys.addAll(this.stepRepository.update(step));
            }
        }
        return redisKeys;
    }

    public List<String> terminatedStepsByTaskId(Long taskId) {
        List<TaskStep> steps = this.stepRepository.findStepesByTaskId(taskId);
        List<String> redisKeys = new ArrayList<>(List.of());
        for(TaskStep step : steps){
            step.setStatus(TaskStep.TERMINATED);
            redisKeys.addAll(this.stepRepository.update(step));
        }
        return redisKeys;
    }
}

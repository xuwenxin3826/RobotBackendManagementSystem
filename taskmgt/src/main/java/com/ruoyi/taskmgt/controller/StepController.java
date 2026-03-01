package com.ruoyi.taskmgt.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.common.validation.NewGroup;
import com.ruoyi.taskmgt.controller.dto.TaskStepDto;
import com.ruoyi.taskmgt.domain.bo.TaskStep;
import com.ruoyi.taskmgt.service.impl.StepServiceImpl;
import com.ruoyi.taskmgt.service.vo.TaskStepVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

public class StepController extends BaseController {
    private StepServiceImpl stepService;
    @Log(title = "创建步骤", businessType = BusinessType.INSERT)
    @PostMapping("tasks/{id}/steps")
    public AjaxResult createTaskSteps(@PathVariable Long taskId,@Validated(value = NewGroup.class) @RequestBody List<TaskStepDto> dtos)
    {
        List<TaskStep> steps = dtos.stream()
                .map(po -> CloneFactory.copy(new TaskStep(), po))
                .collect(Collectors.toList());
        List<TaskStepVo> result = this.stepService.createSteps(taskId,steps);
        return success(result);
    }
}

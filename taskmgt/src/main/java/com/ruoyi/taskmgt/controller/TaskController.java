package com.ruoyi.taskmgt.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.taskmgt.controller.dto.TaskDto;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.service.impl.TaskServiceImpl;
import com.ruoyi.taskmgt.service.vo.TaskVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController extends BaseController {
    private final TaskServiceImpl taskService;
    @Log(title = "任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult createTask(@Validated @RequestBody TaskDto dto)
    {
        Task task = CloneFactory.copy(new Task(),dto);
        TaskVo result = taskService.createTask(task);
        return success(result);
    }
}

package com.ruoyi.taskmgt.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.common.validation.NewGroup;
import com.ruoyi.taskmgt.controller.dto.TaskDto;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.service.impl.TaskServiceImpl;
import com.ruoyi.taskmgt.service.vo.TaskVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController extends BaseController {
    private final TaskServiceImpl taskService;
    @Log(title = "创建任务", businessType = BusinessType.INSERT)
    @PostMapping("user")
    public AjaxResult createTask(@Validated(value = NewGroup.class) @RequestBody TaskDto dto)
    {
        Task task = CloneFactory.copy(new Task(),dto);
        TaskVo result = taskService.createTask(task);
        return success(result);
    }
    @Log(title = "修改任务", businessType = BusinessType.UPDATE)
    @PostMapping("users/{id}")
    public AjaxResult updateTask(@PathVariable Long id,@Validated @RequestBody TaskDto dto)
    {
        Task task = CloneFactory.copy(new Task(),dto);
        task.setId(id);
        taskService.updateTask(task);
        return success();
    }
}

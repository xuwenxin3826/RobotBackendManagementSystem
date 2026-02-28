package com.ruoyi.taskmgt.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskController extends BaseController {
    private final TaskServiceImpl taskService;

    @Log(title = "查看任务列表")
    @GetMapping("tasks")
    public TableDataInfo retrieveTasks(@RequestParam(required = false) Byte status, @RequestParam(required = false) Integer isGroupTask, @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Long robotId, @RequestParam(required = false)Long robotGroupId, @RequestParam(required = false)Integer taskType,
                                       @RequestParam(required = false) Integer riskLevel, @RequestParam(required = false)Long templateId,
                                       @RequestParam(required = false,defaultValue = "1")Integer pageNum, @RequestParam(required = false,defaultValue = "10")Integer pageSize)
    {
        startPage(pageNum,pageSize);
        List<TaskVo> result = this.taskService.retrieveTasks(status,isGroupTask,name,robotId,robotGroupId,taskType, riskLevel, templateId);
        return getDataTable(result);
    }

    /*还需添加返回Vo对象中的机器人名称（需要调用机器人管理模块）*/
    @Log(title = "创建任务", businessType = BusinessType.INSERT)
    @PostMapping("task")
    public AjaxResult createTask(@Validated(value = NewGroup.class) @RequestBody TaskDto dto)
    {
        Task task = CloneFactory.copy(new Task(),dto);
        TaskVo result = this.taskService.createTask(task);
        return success(result);
    }
    @Log(title = "修改任务", businessType = BusinessType.UPDATE)
    @PutMapping("tasks/{id}")
    public AjaxResult updateTask(@PathVariable Long id,@Validated @RequestBody TaskDto dto)
    {
        Task task = CloneFactory.copy(new Task(),dto);
        task.setId(id);
        this.taskService.updateTask(task);
        return success();
    }

    @Log(title = "删除任务", businessType = BusinessType.DELETE)
    @DeleteMapping("tasks/{id}")
    public AjaxResult deleteTask(@PathVariable Long id)
    {
        this.taskService.deleteTask(id);
        return success();
    }

    @Log(title = "查看任务详情")
    @GetMapping("tasks/{id}")
    public AjaxResult getTask(@PathVariable Long id)
    {
        TaskVo result = this.taskService.getTask(id);
        return success(result);
    }

    @Log(title = "禁用任务" ,businessType = BusinessType.UPDATE)
    @PutMapping("tasks/{id}/ban")
    public AjaxResult banTask(@PathVariable Long id)
    {
        this.taskService.banTask(id);
        return success();
    }

    @Log(title = "恢复任务" ,businessType = BusinessType.UPDATE)
    @PutMapping("tasks/{id}/resume")
    public AjaxResult resumeTask(@PathVariable Long id)
    {
        this.taskService.resumeTask(id);
        return success();
    }


}

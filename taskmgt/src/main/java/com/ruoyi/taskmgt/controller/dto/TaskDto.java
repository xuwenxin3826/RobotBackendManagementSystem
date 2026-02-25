package com.ruoyi.taskmgt.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.clonefactory.CopyTo;
import com.ruoyi.common.validation.NewGroup;
import com.ruoyi.taskmgt.domain.bo.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyTo({Task.class})
public class TaskDto {
    /** 任务ID */
    private Long id;

    /** 模板ID（可为空，简单任务没有模板） */
    private Long templateId;

    /** 任务名称 */
    @NotBlank(message = "Dto.NotNull",groups = NewGroup.class)
    private String name;

    /**任务标准作业流程 （JSON格式，前端解析表单生成的任务完整步骤） */
    private String taskContext;

    /** 优先级（1-10，数值越小优先级越高） */
    private Integer priority;

    /** 是否组任务（Y是 N否） */
    private String isGroupTask;

    /** 任务时长（分钟） */
    private Integer duration;

    /** 任务类型（1定时 2电量 3闲时） */
    private String taskType;

    /** 任务状态（0未开始 1准备中 2执行中 3已暂停 4已禁用 5已终止） */
    private String status;

    /** 风险等级（0正常 1风险 2高风险） */
    private String riskLevel;

    /** Cron表达式（定时任务时填写） */
    private String cronExpression;

    /** 电量阈值（电量任务时填写，如 80） */
    private Integer batteryThreshold;

    /** 闲时等待时间（分钟，闲时任务时填写） */
    private Integer idleTime;

    /** 指定机器人ID（非组任务时） */
    private Long robotId;

    /** 指定机器人组ID（组任务时） */
    private Long robotGroupId;

    /** 实际执行的机器人ID（组任务中当前执行该任务的机器人） */
    private Long executeRobotId;

    /** 任务开始时间（定时任务时使用） */
    private Date scheduledTime;
}

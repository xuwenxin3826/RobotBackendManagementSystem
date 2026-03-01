package com.ruoyi.taskmgt.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.clonefactory.CopyTo;
import com.ruoyi.taskmgt.domain.bo.TaskStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.*;
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyTo({TaskStep.class})
public class TaskStepDto {
    private Long id;

    /** 所属任务ID */
    private Long taskId;

    /** 步骤名称 */
    private String stepName;

    /** 具体描述 */
    private String description;

    /** 步骤序号 */
    private Integer orderNum;

    /** 步骤状态（0未开始 1进行中 2已完成 3已暂停 4已终止） */
    private Byte status;
}

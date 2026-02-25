package com.ruoyi.taskmgt.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 机器人任务模板对象 template
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Template extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 模板ID */
    private Long id;

    /** 模板名称 */
    @NotBlank(message = "模板名称不能为空")
    private String name;

    /** 模板描述 */
    private String description;

    /** 关联的机器人组ID（可为空，表示不限制组） */
    private Long robotGroupId;

    /** 表单内容（JSON格式，定义任务参数） */
    private String formContent;

    /** 标准作业流程（JSON数组，定义步骤名称、顺序等） */
    private String workflow;

    /** 状态（0正常 1停用） */
    private String status;
}

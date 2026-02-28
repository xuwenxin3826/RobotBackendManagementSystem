package com.ruoyi.taskmgt.mapper.po;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Table(name="robot_template")
@Entity
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TemplatePo extends BaseEntity {
    @Id
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

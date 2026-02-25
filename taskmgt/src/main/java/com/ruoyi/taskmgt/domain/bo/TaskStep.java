package com.ruoyi.taskmgt.domain.bo;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 任务步骤执行记录对象 task_step
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TaskStep extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 步骤ID */
    private Long id;

    /** 所属任务ID */
    private Long taskId;

    /** 步骤名称 */
    private String stepName;

    /** 步骤序号 */
    private Integer orderNum;

    /** 步骤状态（0未开始 1进行中 2已完成 3失败） */
    private String status;

    /** 执行日志 */
    private String log;

    /** 开始时间 */
    private Date startTime;

    /** 结束时间 */
    private Date endTime;
}

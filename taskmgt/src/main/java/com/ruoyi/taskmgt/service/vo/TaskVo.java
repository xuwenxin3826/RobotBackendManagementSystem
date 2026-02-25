package com.ruoyi.taskmgt.service.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.clonefactory.CopyFrom;
import com.ruoyi.taskmgt.domain.bo.Task;
import lombok.*;

import java.util.Date;


@CopyFrom({Task.class})
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskVo {
    /** 任务ID */
    private Long id;

    /** 模板名称（关联查询） */
    private String templateName;

    /** 任务名称 */
    private String name;

    /** 优先级 */
    private Integer priority;

    /** 是否组任务（Y/N） */
    private String isGroupTask;

    /** 任务时长（分钟） */
    private Integer duration;

    /** 任务类型（中文描述） */
    private String taskTypeText;

    /** 任务状态（中文描述） */
    private String statusText;

    /** 风险等级（中文描述） */
    private String riskLevelText;

    /** Cron表达式 */
    private String cronExpression;

    /** 电量阈值 */
    private Integer batteryThreshold;

    /** 闲时等待时间 */
    private Integer idleTime;

    /** 指定机器人名称 */
    private String robotName;

    /** 指定机器人组名称 */
    private String robotGroupName;

    /** 实际执行机器人名称 */
    private String executeRobotName;

    /** 定时开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduledTime;

    /**任务标准作业流程 （JSON格式，前端解析表单生成的任务完整步骤） */
    private String taskContext;

    /** 任务进度（JSON格式，前端自行解析） */
    private String progress;

    /** 暂停断点信息 */
    private String pauseSnapshot;

    /** 终止原因 */
    private String terminateReason;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
package com.ruoyi.taskmgt.service.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.clonefactory.CopyFrom;
import com.ruoyi.taskmgt.domain.bo.TaskLog;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;

@CopyFrom({TaskLog.class})
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("任务日志VO")
public class TaskLogVo {
    private Long id;
    private Long taskId;
    private String taskName;       // 关联查询的任务名称
    private Long stepId;
    private String stepName;       // 关联查询的步骤名称
    private String eventType;
    private String content;
    private String operator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
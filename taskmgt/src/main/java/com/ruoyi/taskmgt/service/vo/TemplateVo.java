package com.ruoyi.taskmgt.service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.clonefactory.CopyFrom;
import com.ruoyi.taskmgt.domain.bo.Template;
import lombok.*;

import java.util.Date;

@CopyFrom({Template.class})
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString(doNotUseGetters = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateVo {
    private Long id;
    private String name;
    private String description;
    private Long robotGroupId;
    private String formContent;
    private String workflow;
    private Byte status;
    private Date createTime;
    private Date updateTime;
    @Setter
    private String robotGroupName;  // 关联查询填充
}

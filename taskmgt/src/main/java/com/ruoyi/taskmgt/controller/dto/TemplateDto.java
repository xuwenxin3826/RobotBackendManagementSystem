package com.ruoyi.taskmgt.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ruoyi.common.clonefactory.CopyTo;
import com.ruoyi.taskmgt.domain.bo.Template;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@CopyTo({Template.class})
public class TemplateDto {
    private Long id;
    @NotBlank(message = "模板名称不能为空")
    private String name;
    private String description;
    private Long robotGroupId;
    private String formContent;
    private String workflow;
}
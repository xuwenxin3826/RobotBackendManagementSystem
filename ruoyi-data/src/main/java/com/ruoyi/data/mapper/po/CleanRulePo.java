package com.ruoyi.data.mapper.po;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.time.LocalTime;

@Data
public class CleanRulePo extends BaseEntity {

    private Long id;

    private String executeMode;

    private LocalTime runTime;

    private String applyDataSource;

    private String configJson;

    private Integer status;

    // getter / setter
}
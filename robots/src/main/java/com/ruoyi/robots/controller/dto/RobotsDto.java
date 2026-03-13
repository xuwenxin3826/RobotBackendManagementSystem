package com.ruoyi.robots.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

@Data
public class RobotsDto {
    /** 机器人ID */
    private String id;
    /** 机器人编号 */
    private String code;
    /** 机器人名称 */
    @Excel(name = "机器人名称")
    private String name;

    /** 所属分组ID（逻辑外键） */
    @Excel(name = "所属分组ID", readConverterExp = "逻=辑外键")
    private String groupId;

    /** 生产厂家 */
    @Excel(name = "生产厂家")
    private String manufacturer;

    /** 生产日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生产日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date productionDate;

    /** 所属区域 */
    @Excel(name = "所属区域")
    private String area;
}

package com.ruoyi.robots.controller.dto;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

@Data
public class RobotWarningsDto {
    /** 预警ID */
    private String id;


    /** 处理人 */
    private String resolveUser;

    /** 处理备注 */
    private String resolveNote;
}

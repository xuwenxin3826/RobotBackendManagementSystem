package com.ruoyi.taskmgt.service.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RobotStatus {
    private Long robotId;
    private String robotName;
    private String status;
}
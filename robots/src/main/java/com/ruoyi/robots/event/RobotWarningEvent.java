package com.ruoyi.robots.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;


@Setter
@Getter
public class RobotWarningEvent extends ApplicationEvent {
    private Long robotId;// 关联机器人ID
    private String warningType;// 预警类型（0-低电量，1-硬件异常，2-离线）
    private String warningLevel;// 预警级别（0-提示，1-警告，2-错误）
    private String status;// 预警状态（0-待处理，1-已解决）
    private boolean hasRemaining;// 是否还有其他未解决预警

    public RobotWarningEvent(Object source, Long robotId, String warningType,
                             String warningLevel, String status, boolean hasRemaining) {
        super(source);
        this.robotId = robotId;
        this.warningType = warningType;
        this.warningLevel = warningLevel;
        this.status = status;
        this.hasRemaining = hasRemaining;
    }
}
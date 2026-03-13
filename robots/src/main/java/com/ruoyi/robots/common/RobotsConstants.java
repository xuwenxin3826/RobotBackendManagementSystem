package com.ruoyi.robots.common;

public class RobotsConstants {

    //预警状态（0-待处理，1-已解决，2-已忽略
    public static final String UNRESOLVED="0";
    public static final String RESOLVED="1";

    //预警类型（0-低电量，1-硬件异常，2-离线）
    public static final Integer LOWPOWER=0;
    public static final Integer HARDWAREERROR=1;
    public static final Integer OFFLINE=2;

    public static final String GROUP_BY_RELATED_BY_ROBOT = "分组被机器人关联";
    public static final String ROBOT_CODE_HAS_EXISTED = "机器人编号已存在";
    public static final String GROUP_NAME_HAS_EXISTED = "分组名称已存在";
}

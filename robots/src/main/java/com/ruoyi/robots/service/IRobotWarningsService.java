package com.ruoyi.robots.service;

import java.util.List;

import com.ruoyi.robots.controller.dto.RobotWarningsDto;
import com.ruoyi.robots.domain.RobotWarnings;

/**
 * 机器人状态预警Service接口
 * 
 * @author xiaocai
 * @date 2026-03-07
 */
public interface IRobotWarningsService 
{
    /**
     * 查询机器人状态预警
     * 
     * @param id 机器人状态预警主键
     * @return 机器人状态预警
     */
    public RobotWarnings selectRobotWarningsById(Long id);

    /**
     * 查询机器人状态预警列表
     * 
     * @param robotWarnings 机器人状态预警
     * @return 机器人状态预警集合
     */
    public List<RobotWarnings> selectRobotWarningsList(RobotWarnings robotWarnings);

    /**
     * 新增机器人状态预警
     * 
     * @param robotWarnings 机器人状态预警
     * @return 结果
     */
    public int insertRobotWarnings(RobotWarnings robotWarnings);

    /**
     * 修改机器人状态预警
     * 
     * @param robotWarnings 机器人状态预警
     * @return 结果
     */
    public int updateRobotWarnings(RobotWarnings robotWarnings);

    /**
     * 批量删除机器人状态预警
     * 
     * @param ids 需要删除的机器人状态预警主键集合
     * @return 结果
     */
    public int deleteRobotWarningsByIds(Long[] ids);

    /**
     * 删除机器人状态预警信息
     * 
     * @param id 机器人状态预警主键
     * @return 结果
     */
    public int deleteRobotWarningsById(String id);

    int dealRobotWarnings(RobotWarningsDto robotWarningsDto);

    public List<RobotWarnings> selectRobotWarningsListByStatus(String status);

    int countUnresolvedByRobotId(Long robotId);
}

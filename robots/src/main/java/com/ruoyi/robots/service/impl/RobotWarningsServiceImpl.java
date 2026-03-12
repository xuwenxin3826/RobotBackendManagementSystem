package com.ruoyi.robots.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.robots.controller.dto.RobotWarningsDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.robots.mapper.RobotWarningsMapper;
import com.ruoyi.robots.domain.RobotWarnings;
import com.ruoyi.robots.service.IRobotWarningsService;

/**
 * 机器人状态预警Service业务层处理
 * 
 * @author xiaocai
 * @date 2026-03-07
 */
@Service
public class RobotWarningsServiceImpl implements IRobotWarningsService 
{
    @Autowired
    private RobotWarningsMapper robotWarningsMapper;

    /**
     * 查询机器人状态预警
     * 
     * @param id 机器人状态预警主键
     * @return 机器人状态预警
     */
    @Override
    public RobotWarnings selectRobotWarningsById(String id)
    {
        return robotWarningsMapper.selectRobotWarningsById(id);
    }

    /**
     * 查询机器人状态预警列表
     * 
     * @param robotWarnings 机器人状态预警
     * @return 机器人状态预警
     */
    @Override
    public List<RobotWarnings> selectRobotWarningsList(RobotWarnings robotWarnings)
    {
        return robotWarningsMapper.selectRobotWarningsList(robotWarnings);
    }

    /**
     * 新增机器人状态预警
     * 
     * @param robotWarnings 机器人状态预警
     * @return 结果
     */
    @Override
    public int insertRobotWarnings(RobotWarnings robotWarnings)
    {
        return robotWarningsMapper.insertRobotWarnings(robotWarnings);
    }

    /**
     * 修改机器人状态预警
     * 
     * @param robotWarnings 机器人状态预警
     * @return 结果
     */
    @Override
    public int updateRobotWarnings(RobotWarnings robotWarnings)
    {
        return robotWarningsMapper.updateRobotWarnings(robotWarnings);
    }

    /**
     * 批量删除机器人状态预警
     * 
     * @param ids 需要删除的机器人状态预警主键
     * @return 结果
     */
    @Override
    public int deleteRobotWarningsByIds(String[] ids)
    {
        return robotWarningsMapper.deleteRobotWarningsByIds(ids);
    }

    /**
     * 删除机器人状态预警信息
     * 
     * @param id 机器人状态预警主键
     * @return 结果
     */
    @Override
    public int deleteRobotWarningsById(String id)
    {
        return robotWarningsMapper.deleteRobotWarningsById(id);
    }

    @Override
    public int  dealRobotWarnings(RobotWarningsDto robotWarningsDto) {
        RobotWarnings robotWarnings = new RobotWarnings();
        BeanUtils.copyProperties(robotWarningsDto, robotWarnings);
        robotWarnings.setResolveTime(new Date());
        robotWarnings.setStatus("1");
        return robotWarningsMapper.updateRobotWarnings(robotWarnings);
    }

    @Override
    public List<RobotWarnings> selectRobotWarningsListByStatus(String status) {
        RobotWarnings robotWarnings = new RobotWarnings();
        robotWarnings.setStatus(status);
        return robotWarningsMapper.selectRobotWarningsList(robotWarnings);
    }
}

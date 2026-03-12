package com.ruoyi.robots.service.impl;

import java.util.List;

import com.ruoyi.robots.controller.dto.RobotsDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.robots.mapper.RobotsMapper;
import com.ruoyi.robots.domain.Robots;
import com.ruoyi.robots.service.IRobotsService;

/**
 * 机器人基础信息Service业务层处理
 * 
 * @author xiaocai
 * @date 2026-03-07
 */
@Service
public class RobotsServiceImpl implements IRobotsService 
{
    @Autowired
    private RobotsMapper robotsMapper;

    /**
     * 查询机器人基础信息
     * 
     * @param id 机器人基础信息主键
     * @return 机器人基础信息
     */
    @Override
    public Robots selectRobotsById(String id)
    {
        return robotsMapper.selectRobotsById(id);
    }

    /**
     * 查询机器人基础信息列表
     * 
     * @param robots 机器人基础信息
     * @return 机器人基础信息
     */
    @Override
    public List<Robots> selectRobotsList(Robots robots)
    {
        return robotsMapper.selectRobotsList(robots);
    }

    /**
     * 新增机器人基础信息
     * 
     * @param robots 机器人基础信息
     * @return 结果
     */
    @Override
    public int insertRobots(Robots robots)
    {
        return robotsMapper.insertRobots(robots);
    }

    /**
     * 修改机器人基础信息
     *
     * @return 结果
     */
    @Override
    public int updateRobots(RobotsDto robotsDto)
    {
        Robots robots = new Robots();
        BeanUtils.copyProperties(robotsDto, robots);
        return robotsMapper.updateRobots(robots);
    }

    /**
     * 批量删除机器人基础信息
     * 
     * @param ids 需要删除的机器人基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRobotsByIds(String[] ids)
    {
        return robotsMapper.deleteRobotsByIds(ids);
    }

    /**
     * 删除机器人基础信息信息
     * 
     * @param id 机器人基础信息主键
     * @return 结果
     */
    @Override
    public int deleteRobotsById(String id)
    {
        return robotsMapper.deleteRobotsById(id);
    }
}

package com.ruoyi.robots.mapper;

import java.util.List;
import com.ruoyi.robots.domain.Robots;
import org.apache.ibatis.annotations.Select;

/**
 * 机器人基础信息Mapper接口
 * 
 * @author xiaocai
 * @date 2026-03-07
 */
public interface RobotsMapper 
{
    /**
     * 查询机器人基础信息
     * 
     * @param id 机器人基础信息主键
     * @return 机器人基础信息
     */
    public Robots selectRobotsById(String id);

    /**
     * 查询机器人基础信息列表
     * 
     * @param robots 机器人基础信息
     * @return 机器人基础信息集合
     */
    public List<Robots> selectRobotsList(Robots robots);

    /**
     * 新增机器人基础信息
     * 
     * @param robots 机器人基础信息
     * @return 结果
     */
    public int insertRobots(Robots robots);

    /**
     * 修改机器人基础信息
     * 
     * @param robots 机器人基础信息
     * @return 结果
     */
    public int updateRobots(Robots robots);

    /**
     * 删除机器人基础信息
     * 
     * @param id 机器人基础信息主键
     * @return 结果
     */
    public int deleteRobotsById(String id);

    /**
     * 批量删除机器人基础信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRobotsByIds(String[] ids);

    @Select("select count(id) from robots where group_id = #{groupId}")
    public Integer countByGroupId(String categoryId);

    @Select("select count(id) from robots where group_id = #{groupId}")
    int selectRobotsByCode(String code);
}

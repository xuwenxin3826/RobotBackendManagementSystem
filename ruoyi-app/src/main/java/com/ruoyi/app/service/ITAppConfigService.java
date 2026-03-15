package com.ruoyi.app.service;

import java.util.List;
import com.ruoyi.app.domain.TAppConfig;

/**
 * 应用配置Service接口
 * 
 * @author xiaocai
 * @date 2026-03-13
 */
public interface ITAppConfigService 
{
    /**
     * 查询应用配置
     * 
     * @param id 应用配置主键
     * @return 应用配置
     */
    public TAppConfig selectTAppConfigById(Long id);

    /**
     * 查询应用配置列表
     * 
     * @param tAppConfig 应用配置
     * @return 应用配置集合
     */
    public List<TAppConfig> selectTAppConfigList(TAppConfig tAppConfig);

    /**
     * 新增应用配置
     * 
     * @param tAppConfig 应用配置
     * @return 结果
     */
    public int insertTAppConfig(TAppConfig tAppConfig);

    /**
     * 修改应用配置
     * 
     * @param tAppConfig 应用配置
     * @return 结果
     */
    public int updateTAppConfig(TAppConfig tAppConfig);

    /**
     * 批量删除应用配置
     * 
     * @param ids 需要删除的应用配置主键集合
     * @return 结果
     */
    public int deleteTAppConfigByIds(Long[] ids);

    /**
     * 删除应用配置信息
     * 
     * @param id 应用配置主键
     * @return 结果
     */
    public int deleteTAppConfigById(Long id);
}

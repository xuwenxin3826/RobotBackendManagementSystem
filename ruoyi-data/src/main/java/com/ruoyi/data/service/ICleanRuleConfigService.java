package com.ruoyi.data.service;

import java.util.List;
import com.ruoyi.data.domain.CleanRuleConfig;

/**
 * 数据清洗规则配置Service接口
 * 
 * @author chenruyi
 * @date 2026-02-23
 */
public interface ICleanRuleConfigService 
{
    /**
     * 查询数据清洗规则配置
     * 
     * @param id 数据清洗规则配置主键
     * @return 数据清洗规则配置
     */
    public CleanRuleConfig selectCleanRuleConfigById(Long id);

    /**
     * 查询数据清洗规则配置列表
     * 
     * @param cleanRuleConfig 数据清洗规则配置
     * @return 数据清洗规则配置集合
     */
    public List<CleanRuleConfig> selectCleanRuleConfigList(CleanRuleConfig cleanRuleConfig);

    /**
     * 新增数据清洗规则配置
     * 
     * @param cleanRuleConfig 数据清洗规则配置
     * @return 结果
     */
    public int insertCleanRuleConfig(CleanRuleConfig cleanRuleConfig);

    /**
     * 修改数据清洗规则配置
     * 
     * @param cleanRuleConfig 数据清洗规则配置
     * @return 结果
     */
    public int updateCleanRuleConfig(CleanRuleConfig cleanRuleConfig);

    /**
     * 批量删除数据清洗规则配置
     * 
     * @param ids 需要删除的数据清洗规则配置主键集合
     * @return 结果
     */
    public int deleteCleanRuleConfigByIds(Long[] ids);

    /**
     * 删除数据清洗规则配置信息
     * 
     * @param id 数据清洗规则配置主键
     * @return 结果
     */
    public int deleteCleanRuleConfigById(Long id);
}

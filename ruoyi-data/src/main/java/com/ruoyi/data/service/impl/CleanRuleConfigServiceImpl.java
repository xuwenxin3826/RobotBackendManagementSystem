package com.ruoyi.data.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.data.mapper.CleanRuleConfigMapper;
import com.ruoyi.data.domain.CleanRuleConfig;
import com.ruoyi.data.service.ICleanRuleConfigService;

/**
 * 数据清洗规则配置Service业务层处理
 * 
 * @author chenruyi
 * @date 2026-02-23
 */
@Service
public class CleanRuleConfigServiceImpl implements ICleanRuleConfigService 
{
    @Autowired
    private CleanRuleConfigMapper cleanRuleConfigMapper;

    /**
     * 查询数据清洗规则配置
     * 
     * @param id 数据清洗规则配置主键
     * @return 数据清洗规则配置
     */
    @Override
    public CleanRuleConfig selectCleanRuleConfigById(Long id)
    {
        return cleanRuleConfigMapper.selectCleanRuleConfigById(id);
    }

    /**
     * 查询数据清洗规则配置列表
     * 
     * @param cleanRuleConfig 数据清洗规则配置
     * @return 数据清洗规则配置
     */
    @Override
    public List<CleanRuleConfig> selectCleanRuleConfigList(CleanRuleConfig cleanRuleConfig)
    {
        return cleanRuleConfigMapper.selectCleanRuleConfigList(cleanRuleConfig);
    }

    /**
     * 新增数据清洗规则配置
     * 
     * @param cleanRuleConfig 数据清洗规则配置
     * @return 结果
     */
    @Override
    public int insertCleanRuleConfig(CleanRuleConfig cleanRuleConfig)
    {
        return cleanRuleConfigMapper.insertCleanRuleConfig(cleanRuleConfig);
    }

    /**
     * 修改数据清洗规则配置
     * 
     * @param cleanRuleConfig 数据清洗规则配置
     * @return 结果
     */
    @Override
    public int updateCleanRuleConfig(CleanRuleConfig cleanRuleConfig)
    {
        return cleanRuleConfigMapper.updateCleanRuleConfig(cleanRuleConfig);
    }

    /**
     * 批量删除数据清洗规则配置
     * 
     * @param ids 需要删除的数据清洗规则配置主键
     * @return 结果
     */
    @Override
    public int deleteCleanRuleConfigByIds(Long[] ids)
    {
        return cleanRuleConfigMapper.deleteCleanRuleConfigByIds(ids);
    }

    /**
     * 删除数据清洗规则配置信息
     * 
     * @param id 数据清洗规则配置主键
     * @return 结果
     */
    @Override
    public int deleteCleanRuleConfigById(Long id)
    {
        return cleanRuleConfigMapper.deleteCleanRuleConfigById(id);
    }
}

package com.ruoyi.app.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.app.mapper.TAppUpdateMapper;
import com.ruoyi.app.domain.TAppUpdate;
import com.ruoyi.app.service.ITAppUpdateService;

/**
 * 应用更新记录Service业务层处理
 * 
 * @author xiaocai
 * @date 2026-03-13
 */
@Service
public class TAppUpdateServiceImpl implements ITAppUpdateService 
{
    @Autowired
    private TAppUpdateMapper tAppUpdateMapper;

    /**
     * 查询应用更新记录
     * 
     * @param id 应用更新记录主键
     * @return 应用更新记录
     */
    @Override
    public TAppUpdate selectTAppUpdateById(Long id)
    {
        return tAppUpdateMapper.selectTAppUpdateById(id);
    }

    /**
     * 查询应用更新记录列表
     * 
     * @param tAppUpdate 应用更新记录
     * @return 应用更新记录
     */
    @Override
    public List<TAppUpdate> selectTAppUpdateList(TAppUpdate tAppUpdate)
    {
        return tAppUpdateMapper.selectTAppUpdateList(tAppUpdate);
    }

    /**
     * 新增应用更新记录
     * 
     * @param tAppUpdate 应用更新记录
     * @return 结果
     */
    @Override
    public int insertTAppUpdate(TAppUpdate tAppUpdate)
    {
        tAppUpdate.setCreateTime(DateUtils.getNowDate());
        return tAppUpdateMapper.insertTAppUpdate(tAppUpdate);
    }

    /**
     * 修改应用更新记录
     * 
     * @param tAppUpdate 应用更新记录
     * @return 结果
     */
    @Override
    public int updateTAppUpdate(TAppUpdate tAppUpdate)
    {
        tAppUpdate.setUpdateTime(DateUtils.getNowDate());
        return tAppUpdateMapper.updateTAppUpdate(tAppUpdate);
    }

    /**
     * 批量删除应用更新记录
     * 
     * @param ids 需要删除的应用更新记录主键
     * @return 结果
     */
    @Override
    public int deleteTAppUpdateByIds(Long[] ids)
    {
        return tAppUpdateMapper.deleteTAppUpdateByIds(ids);
    }

    /**
     * 删除应用更新记录信息
     * 
     * @param id 应用更新记录主键
     * @return 结果
     */
    @Override
    public int deleteTAppUpdateById(Long id)
    {
        return tAppUpdateMapper.deleteTAppUpdateById(id);
    }
}

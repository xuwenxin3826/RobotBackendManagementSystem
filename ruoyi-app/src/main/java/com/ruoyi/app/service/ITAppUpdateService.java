package com.ruoyi.app.service;

import java.util.List;
import com.ruoyi.app.domain.TAppUpdate;

/**
 * 应用更新记录Service接口
 * 
 * @author xiaocai
 * @date 2026-03-13
 */
public interface ITAppUpdateService 
{
    /**
     * 查询应用更新记录
     * 
     * @param id 应用更新记录主键
     * @return 应用更新记录
     */
    public TAppUpdate selectTAppUpdateById(Long id);

    /**
     * 查询应用更新记录列表
     * 
     * @param tAppUpdate 应用更新记录
     * @return 应用更新记录集合
     */
    public List<TAppUpdate> selectTAppUpdateList(TAppUpdate tAppUpdate);

    /**
     * 新增应用更新记录
     * 
     * @param tAppUpdate 应用更新记录
     * @return 结果
     */
    public int insertTAppUpdate(TAppUpdate tAppUpdate);

    /**
     * 修改应用更新记录
     * 
     * @param tAppUpdate 应用更新记录
     * @return 结果
     */
    public int updateTAppUpdate(TAppUpdate tAppUpdate);

    /**
     * 批量删除应用更新记录
     * 
     * @param ids 需要删除的应用更新记录主键集合
     * @return 结果
     */
    public int deleteTAppUpdateByIds(Long[] ids);

    /**
     * 删除应用更新记录信息
     * 
     * @param id 应用更新记录主键
     * @return 结果
     */
    public int deleteTAppUpdateById(Long id);
}

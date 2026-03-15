package com.ruoyi.app.mapper;

import java.util.List;
import com.ruoyi.app.domain.TAppUpdate;

/**
 * 应用更新记录Mapper接口
 * 
 * @author xiaocai
 * @date 2026-03-13
 */
public interface TAppUpdateMapper 
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
     * 删除应用更新记录
     * 
     * @param id 应用更新记录主键
     * @return 结果
     */
    public int deleteTAppUpdateById(Long id);

    /**
     * 批量删除应用更新记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTAppUpdateByIds(Long[] ids);
}

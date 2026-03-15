package com.ruoyi.app.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.app.mapper.TAppLibraryMapper;
import com.ruoyi.app.domain.TAppLibrary;
import com.ruoyi.app.service.ITAppLibraryService;

/**
 * 应用库Service业务层处理
 * 
 * @author xiaocai
 * @date 2026-03-13
 */
@Service
public class TAppLibraryServiceImpl implements ITAppLibraryService 
{
    @Autowired
    private TAppLibraryMapper tAppLibraryMapper;

    /**
     * 查询应用库
     * 
     * @param id 应用库主键
     * @return 应用库
     */
    @Override
    public TAppLibrary selectTAppLibraryById(Long id)
    {
        return tAppLibraryMapper.selectTAppLibraryById(id);
    }

    /**
     * 查询应用库列表
     * 
     * @param tAppLibrary 应用库
     * @return 应用库
     */
    @Override
    public List<TAppLibrary> selectTAppLibraryList(TAppLibrary tAppLibrary)
    {
        return tAppLibraryMapper.selectTAppLibraryList(tAppLibrary);
    }

    /**
     * 新增应用库
     * 
     * @param tAppLibrary 应用库
     * @return 结果
     */
    @Override
    public int insertTAppLibrary(TAppLibrary tAppLibrary)
    {
        tAppLibrary.setCreateTime(DateUtils.getNowDate());
        return tAppLibraryMapper.insertTAppLibrary(tAppLibrary);
    }

    /**
     * 修改应用库
     * 
     * @param tAppLibrary 应用库
     * @return 结果
     */
    @Override
    public int updateTAppLibrary(TAppLibrary tAppLibrary)
    {
        tAppLibrary.setUpdateTime(DateUtils.getNowDate());
        return tAppLibraryMapper.updateTAppLibrary(tAppLibrary);
    }

    /**
     * 批量删除应用库
     * 
     * @param ids 需要删除的应用库主键
     * @return 结果
     */
    @Override
    public int deleteTAppLibraryByIds(Long[] ids)
    {
        return tAppLibraryMapper.deleteTAppLibraryByIds(ids);
    }

    /**
     * 删除应用库信息
     * 
     * @param id 应用库主键
     * @return 结果
     */
    @Override
    public int deleteTAppLibraryById(Long id)
    {
        return tAppLibraryMapper.deleteTAppLibraryById(id);
    }
}

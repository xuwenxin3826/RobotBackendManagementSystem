package com.ruoyi.app.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.app.domain.TAppLibrary;
import com.ruoyi.app.service.ITAppLibraryService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 应用库Controller
 * 
 * @author xiaocai
 * @date 2026-03-13
 */
@RestController
@RequestMapping("/app/appLibrary")
public class TAppLibraryController extends BaseController
{
    @Autowired
    private ITAppLibraryService tAppLibraryService;

    /**
     * 查询应用库列表
     */
    @PreAuthorize("@ss.hasPermi('app:appLibrary:list')")
    @GetMapping("/list")
    public TableDataInfo list(TAppLibrary tAppLibrary)
    {
        startPage();
        List<TAppLibrary> list = tAppLibraryService.selectTAppLibraryList(tAppLibrary);
        return getDataTable(list);
    }

    /**
     * 导出应用库列表
     */
    @PreAuthorize("@ss.hasPermi('app:appLibrary:export')")
    @Log(title = "应用库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TAppLibrary tAppLibrary)
    {
        List<TAppLibrary> list = tAppLibraryService.selectTAppLibraryList(tAppLibrary);
        ExcelUtil<TAppLibrary> util = new ExcelUtil<TAppLibrary>(TAppLibrary.class);
        util.exportExcel(response, list, "应用库数据");
    }

    /**
     * 获取应用库详细信息
     */
    @PreAuthorize("@ss.hasPermi('app:appLibrary:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tAppLibraryService.selectTAppLibraryById(id));
    }

    /**
     * 新增应用库
     */
    @PreAuthorize("@ss.hasPermi('app:appLibrary:add')")
    @Log(title = "应用库", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TAppLibrary tAppLibrary)
    {
        return toAjax(tAppLibraryService.insertTAppLibrary(tAppLibrary));
    }

    /**
     * 修改应用库
     */
    @PreAuthorize("@ss.hasPermi('app:appLibrary:edit')")
    @Log(title = "应用库", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TAppLibrary tAppLibrary)
    {
        return toAjax(tAppLibraryService.updateTAppLibrary(tAppLibrary));
    }

    /**
     * 删除应用库
     */
    @PreAuthorize("@ss.hasPermi('app:appLibrary:remove')")
    @Log(title = "应用库", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tAppLibraryService.deleteTAppLibraryByIds(ids));
    }
}

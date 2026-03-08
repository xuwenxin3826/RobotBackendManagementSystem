package com.ruoyi.data.controller;

import com.ruoyi.data.domain.CleanExecuteHistory;
import com.ruoyi.data.service.CleanExecuteHistoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/clean/history")
public class CleanExecuteHistoryController {

    @Resource
    private CleanExecuteHistoryService service;

    /**
     * 新增执行记录
     */
    @PostMapping("/create")
    public Long create(@RequestBody CleanExecuteHistory history) {
        return service.createRecord(history);
    }

    /**
     * 查询详情
     */
    @GetMapping("/{id}")
    public CleanExecuteHistory getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * 查询全部
     */
    @GetMapping("/list")
    public List<CleanExecuteHistory> listAll() {
        return service.listAll();
    }

    /**
     * 更新状态
     */
    @PutMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam Integer status) {
        service.updateStatus(id, status);
        return "success";
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "success";
    }
}
package com.ruoyi.data.controller;

import com.ruoyi.data.service.ICleanRuleExecuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clean/execute")
public class CleanRuleExecuteController {

    @Autowired
    private ICleanRuleExecuteService executeService;

    /**
     * 立即执行清洗任务
     */
    @PostMapping("/{id}")
    public String execute(@PathVariable("id") Long id) {

        executeService.executeTask(id);

        return "执行成功";
    }
}
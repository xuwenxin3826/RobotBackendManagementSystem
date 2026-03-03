package com.ruoyi.taskmgt.service;

import com.ruoyi.taskmgt.domain.bo.TaskLog;
import com.ruoyi.taskmgt.service.vo.TaskLogVo;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ITaskLogService {
    @Transactional(readOnly = true)
    public List<TaskLogVo> queryLogs(TaskLog query, String beginTime, String endTime);


    @Transactional(readOnly = true)
    public TaskLogVo getLog(Long id);
}

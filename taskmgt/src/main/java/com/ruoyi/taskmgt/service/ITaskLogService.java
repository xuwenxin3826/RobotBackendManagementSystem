package com.ruoyi.taskmgt.service;

import com.ruoyi.taskmgt.domain.bo.TaskLog;
import com.ruoyi.taskmgt.service.vo.TaskLogVo;

import java.util.List;

public interface ITaskLogService {
    List<TaskLogVo> queryLogs(TaskLog query, String beginTime, String endTime);

    TaskLogVo getLog(Long id);
}

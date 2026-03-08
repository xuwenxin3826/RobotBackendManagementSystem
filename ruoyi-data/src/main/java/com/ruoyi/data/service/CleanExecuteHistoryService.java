package com.ruoyi.data.service;

import com.ruoyi.data.domain.CleanExecuteHistory;

import java.util.List;

public interface CleanExecuteHistoryService {

    Long createRecord(CleanExecuteHistory history);

    CleanExecuteHistory getById(Long id);

    List<CleanExecuteHistory> listAll();

    void updateStatus(Long id, Integer status);

    void delete(Long id);
}
package com.ruoyi.taskmgt.mapper;

import com.ruoyi.taskmgt.mapper.po.TaskLogPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskLogPoMapper extends JpaRepository<TaskLogPo, Long>, JpaSpecificationExecutor<TaskLogPo> {
}
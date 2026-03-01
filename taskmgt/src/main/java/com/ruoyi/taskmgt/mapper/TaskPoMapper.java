package com.ruoyi.taskmgt.mapper;

import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.mapper.po.TaskPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskPoMapper extends JpaRepository<TaskPo, Long>, JpaSpecificationExecutor<TaskPo> {
    Optional<TaskPo> findByName(String userName);

    List<TaskPo> findByRobotIdAndStatus(Long robotId, Byte status);
}

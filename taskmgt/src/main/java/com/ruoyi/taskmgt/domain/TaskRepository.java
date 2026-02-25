package com.ruoyi.taskmgt.domain;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.mapper.TaskPoMapper;
import com.ruoyi.taskmgt.mapper.po.TaskPo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class TaskRepository {
    private final TaskPoMapper taskPoMapper;
    private final RedisCache redisUtil;
    public TaskRepository(TaskPoMapper taskPoMapper, RedisCache redisUtil) {
        this.taskPoMapper = taskPoMapper;
        this.redisUtil = redisUtil;
    }

    /**
     * 构造满血的User对象
     *
     * @param bo 充血的user对象
     * @return 满血的User对象
     */
    private Task build(Task bo) {
        return bo;
    }

    /**
     * 构成满血的User对象
     *
     * @param po       User Po 对象
     * @param redisKey redis key
     * @return 满血的User对象
     */
    private Task build(TaskPo po, Optional<String> redisKey) {
        if (Objects.nonNull(po)) {
            Task bo = CloneFactory.copy(new Task(), po);
            redisKey.ifPresent(key -> this.redisUtil.setCacheObject(key, bo));
            return this.build(bo);
        }
        return null;
    }
    public Task insert(Task task) {
        Assert.notNull(task, "TaskRepository.insert: task can not be null.");
        task.setId(null);
        TaskPo taskPo = CloneFactory.copyNotNull(new TaskPo(), task);

        try {
            TaskPo savedPo = this.taskPoMapper.save(taskPo);
            return CloneFactory.copy(new Task(), savedPo);
        } catch (DataIntegrityViolationException e) {
            String msg = e.getMessage();
            log.debug("TaskRepository:insert: msg={}", msg);
            throw e;
        }

    }
}

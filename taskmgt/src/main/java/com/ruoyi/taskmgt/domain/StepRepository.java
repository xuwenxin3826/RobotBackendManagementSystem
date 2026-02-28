package com.ruoyi.taskmgt.domain;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.taskmgt.domain.bo.TaskStep;
import com.ruoyi.taskmgt.mapper.StepPoMapper;
import com.ruoyi.taskmgt.mapper.po.TaskStepPo;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StepRepository {
    private final StepPoMapper stepPoMapper;
    private final TaskRepository taskRepository;
    private final RedisCache redisUtil;
    public final static String KEY = "R%d";

    public StepRepository(StepPoMapper stepPoMapper, TaskRepository taskRepository, RedisCache redisUtil) {
        this.stepPoMapper = stepPoMapper;
        this.taskRepository = taskRepository;
        this.redisUtil = redisUtil;
    }

    /**
     * 构造满血的TaskStep对象
     *
     * @param bo 充血的taskStep对象
     * @return 满血的TaskStep对象
     */
    private TaskStep build(TaskStep bo) {
        if (Objects.nonNull(bo)){
            bo.setStepRepository(this);
            bo.setTaskRepository(this.taskRepository);
        }
        return bo;
    }

    /**
     * 构成满血的Task对象
     *
     * @param po       Task Po 对象
     * @param redisKey redis key
     * @return 满血的Task对象
     */
    private TaskStep build(TaskStepPo po, Optional<String> redisKey) {
        if (Objects.nonNull(po)) {
            TaskStep bo = CloneFactory.copy(new TaskStep(), po);
            redisKey.ifPresent(key -> this.redisUtil.setCacheObject(key, bo));
            return this.build(bo);
        }
        return null;
    }

    public List<TaskStep> retrieveStepesByTaskId(Long taskId) {
        List<TaskStepPo>taskStepPos = this.stepPoMapper.findByTaskId(taskId);
        return taskStepPos.stream()
                .map(po -> build(po, Optional.empty()))
                .collect(Collectors.toList());
    }

    public List<String> delete(Long id) {
        Assert.notNull(id, "StepRepository.delete: id must not be null");
        String key = String.format(KEY, id);
        this.stepPoMapper.deleteById(id);
        return List.of(key);
    }
}

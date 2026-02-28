package com.ruoyi.taskmgt.domain;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ReturnNo;
import com.ruoyi.common.exception.task.TaskmgtException;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.taskmgt.domain.bo.Task;
import com.ruoyi.taskmgt.domain.bo.Template;
import com.ruoyi.taskmgt.mapper.TemplatePoMapper;
import com.ruoyi.taskmgt.mapper.po.TaskPo;
import com.ruoyi.taskmgt.mapper.po.TemplatePo;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;

@Repository
public class TemplateRepository {
    private static final String TEMPLATEBYID = "TM%d";
    private final RedisCache redisUtil;
    private final TemplatePoMapper templatePoMapper;
    private final MessageSourceAccessor messageSourceAccessor;

    public TemplateRepository(RedisCache redisUtil, TemplatePoMapper templatePoMapper, MessageSourceAccessor messageSourceAccessor) {
        this.redisUtil = redisUtil;
        this.templatePoMapper = templatePoMapper;
        this.messageSourceAccessor = messageSourceAccessor;
    }

    /**
     * 构造满血的Template对象
     *
     * @param bo 充血的template对象
     * @return 满血的Template对象
     */
    private Template build(Template bo) {
        if (Objects.nonNull(bo)){
            bo.setTemplateRepository(this);
        }
        return bo;
    }

    /**
     * 构成满血的Template对象
     *
     * @param po       Template Po 对象
     * @param redisKey redis key
     * @return 满血的Template对象
     */
    private Template build(TemplatePo po, Optional<String> redisKey) {
        if (Objects.nonNull(po)) {
            Template bo = CloneFactory.copy(new Template(), po);
            redisKey.ifPresent(key -> this.redisUtil.setCacheObject(key, bo));
            return this.build(bo);
        }
        return null;
    }

    /**
     * 以id找对象
     *
     * @param id 对象id
     * @return task对象
     */
    public Optional<Template> findById(Long id) {
        Assert.notNull(id, "TemplateRepository.findById: TemplateRepository.findById: id is null");
        String key = String.format(TEMPLATEBYID, id);
        Template bo = (Template) this.redisUtil.getCacheObject(key);
        if (Objects.isNull(bo)) {
            return this.templatePoMapper.findById(id).map(po -> this.build(po, Optional.of(key)));
        } else {
            this.build(bo);
            return Optional.of(bo);
        }
    }

    public String getTemplateNameById(Long id){
        Template template = this.findById(id).orElseThrow(()-> {
            String[] args = new String[]{this.messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()), id.toString()};
            return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,this.messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
        });
        return template.getName();
    }
}

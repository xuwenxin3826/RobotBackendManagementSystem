package com.ruoyi.taskmgt.service.impl;

import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.ReturnNo;
import com.ruoyi.common.exception.task.TaskmgtException;
import com.ruoyi.common.utils.CloneFactory;
import com.ruoyi.taskmgt.domain.TemplateRepository;
import com.ruoyi.taskmgt.domain.bo.Template;
import com.ruoyi.taskmgt.service.vo.TemplateVo;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TemplateServiceImpl {
    private final TemplateRepository templateRepository;
    private final MessageSourceAccessor messageSourceAccessor;
    private final RedisCache redisUtil;

    public TemplateServiceImpl(TemplateRepository templateRepository,
                               MessageSourceAccessor messageSourceAccessor,
                               RedisCache redisUtil) {
        this.templateRepository = templateRepository;
        this.messageSourceAccessor = messageSourceAccessor;
        this.redisUtil = redisUtil;
    }

    public TemplateVo createTemplate(Template template) {
        template.setStatus(Template.ENABLED);
        Template newTemplate = templateRepository.insert(template);
        return CloneFactory.copy(new TemplateVo(), newTemplate);
    }

    public void updateTemplate(Template template) {
        Template existing = templateRepository.findById(template.getId())
                .orElseThrow(() -> {
                    String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()), template.getId().toString()};
                    return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,
                            messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
                });
        if (!Objects.equals(existing.getStatus(), Template.ENABLED) &&
                !Objects.equals(existing.getStatus(), Template.DISABLED)) {
            String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()),
                    existing.getId().toString(), existing.getStatus().toString()};
            throw new TaskmgtException(ReturnNo.STATENOTALLOW, args,
                    messageSourceAccessor.getMessage(ReturnNo.STATENOTALLOW.getMessage()));
        }
        List<String> redisKeys = templateRepository.update(template);
        redisUtil.deleteObject(redisKeys);
    }

    public void deleteTemplate(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> {
                    String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()), id.toString()};
                    return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,
                            messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
                });
        if (!Objects.equals(template.getStatus(), Template.DISABLED)) {
            String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()),
                    id.toString(), template.getStatus().toString()};
            throw new TaskmgtException(ReturnNo.STATENOTALLOW, args,
                    messageSourceAccessor.getMessage(ReturnNo.STATENOTALLOW.getMessage()));
        }
        List<String> redisKeys = updateTemplateStatus(template, Template.DELETED);
        redisUtil.deleteObject(redisKeys);
    }

    public void banTemplate(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> {
                    String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()), id.toString()};
                    return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,
                            messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
                });
        List<String> redisKeys = updateTemplateStatus(template, Template.DISABLED);
        redisUtil.deleteObject(redisKeys);
    }

    public void resumeTemplate(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> {
                    String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()), id.toString()};
                    return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,
                            messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
                });
        if (!Objects.equals(template.getStatus(), Template.DISABLED)) {
            String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()),
                    id.toString(), template.getStatus().toString()};
            throw new TaskmgtException(ReturnNo.STATENOTALLOW, args,
                    messageSourceAccessor.getMessage(ReturnNo.STATENOTALLOW.getMessage()));
        }
        List<String> redisKeys = updateTemplateStatus(template, Template.ENABLED);
        redisUtil.deleteObject(redisKeys);
    }

    private List<String> updateTemplateStatus(Template template, Byte newStatus) {
        if (Objects.nonNull(template) && template.allowTransitStatus(newStatus)) {
            template.setStatus(newStatus);
            return templateRepository.update(template);
        } else {
            String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()),
                    template.getId().toString(), template.getStatus().toString()};
            throw new TaskmgtException(ReturnNo.STATENOTALLOW, args,
                    messageSourceAccessor.getMessage(ReturnNo.STATENOTALLOW.getMessage()));
        }
    }

    public List<TemplateVo> retrieveTemplates(String name, Byte status, Long robotGroupId) {
        List<Template> templates = templateRepository.getTemplates(name, status, robotGroupId);
        return templates.stream()
                .map(template -> {
                    TemplateVo vo = CloneFactory.copy(new TemplateVo(), template);
                    // vo.setRobotGroupName(...);  // 可通过Feign调用填充
                    return vo;
                })
                .collect(Collectors.toList());
    }

    public TemplateVo getTemplate(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> {
                    String[] args = {messageSourceAccessor.getMessage("Template.name", LocaleContextHolder.getLocale()), id.toString()};
                    return new TaskmgtException(ReturnNo.RESOURCE_ID_NOTEXIST, args,
                            messageSourceAccessor.getMessage(ReturnNo.RESOURCE_ID_NOTEXIST.getMessage()));
                });
        TemplateVo vo = CloneFactory.copy(new TemplateVo(), template);
        // vo.setRobotGroupName(...);
        return vo;
    }
}
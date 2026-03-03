package com.ruoyi.taskmgt.service;


import com.ruoyi.taskmgt.domain.bo.Template;
import com.ruoyi.taskmgt.service.vo.TemplateVo;

import java.util.List;

public interface ITemplateService {
    public TemplateVo createTemplate(Template template);

    public void updateTemplate(Template template);

    public void deleteTemplate(Long id);

    public void banTemplate(Long id);

    public void resumeTemplate(Long id);

    public List<TemplateVo> retrieveTemplates(String name, Byte status, Long robotGroupId);

    public TemplateVo getTemplate(Long id);
}

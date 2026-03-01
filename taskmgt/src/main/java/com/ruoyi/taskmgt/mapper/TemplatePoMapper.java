package com.ruoyi.taskmgt.mapper;

import com.ruoyi.taskmgt.mapper.po.TemplatePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TemplatePoMapper extends JpaRepository<TemplatePo, Long> , JpaSpecificationExecutor<TemplatePo> {
}

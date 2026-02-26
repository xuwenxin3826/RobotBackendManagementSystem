package com.ruoyi.data.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 数据清洗规则配置对象 clean_rule_config
 * 
 * @author chenruyi
 * @date 2026-02-23
 */
public class CleanRuleConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    private Long id;

    /** 字段完整性校验 */
    @Excel(name = "字段完整性校验")
    private String fieldIntegrity;

    /** 时间格式统一 */
    @Excel(name = "时间格式统一")
    private String timeFormat;

    /** 状态码映射规则 */
    @Excel(name = "状态码映射规则")
    private String statusMapping;

    /** 异常值过滤策略 */
    @Excel(name = "异常值过滤策略")
    private String outlierFilter;

    /** 文本数据清洗 */
    @Excel(name = "文本数据清洗")
    private String textCleaning;

    /** 重复数据处理 */
    @Excel(name = "重复数据处理")
    private String duplicateHandling;

    /** 执行方式 */
    @Excel(name = "执行方式")
    private String executeMode;

    /** 应用数据源 */
    @Excel(name = "应用数据源")
    private String applyData;

    /** 执行时间 */
    @Excel(name = "执行时间")
    private String runTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setFieldIntegrity(String fieldIntegrity) 
    {
        this.fieldIntegrity = fieldIntegrity;
    }

    public String getFieldIntegrity() 
    {
        return fieldIntegrity;
    }

    public void setTimeFormat(String timeFormat) 
    {
        this.timeFormat = timeFormat;
    }

    public String getTimeFormat() 
    {
        return timeFormat;
    }

    public void setStatusMapping(String statusMapping) 
    {
        this.statusMapping = statusMapping;
    }

    public String getStatusMapping() 
    {
        return statusMapping;
    }

    public void setOutlierFilter(String outlierFilter) 
    {
        this.outlierFilter = outlierFilter;
    }

    public String getOutlierFilter() 
    {
        return outlierFilter;
    }

    public void setTextCleaning(String textCleaning) 
    {
        this.textCleaning = textCleaning;
    }

    public String getTextCleaning() 
    {
        return textCleaning;
    }

    public void setDuplicateHandling(String duplicateHandling) 
    {
        this.duplicateHandling = duplicateHandling;
    }

    public String getDuplicateHandling() 
    {
        return duplicateHandling;
    }

    public void setExecuteMode(String executeMode) 
    {
        this.executeMode = executeMode;
    }

    public String getExecuteMode() 
    {
        return executeMode;
    }

    public void setApplyData(String applyData) 
    {
        this.applyData = applyData;
    }

    public String getApplyData() 
    {
        return applyData;
    }

    public void setRunTime(String runTime) 
    {
        this.runTime = runTime;
    }

    public String getRunTime() 
    {
        return runTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fieldIntegrity", getFieldIntegrity())
            .append("timeFormat", getTimeFormat())
            .append("statusMapping", getStatusMapping())
            .append("outlierFilter", getOutlierFilter())
            .append("textCleaning", getTextCleaning())
            .append("duplicateHandling", getDuplicateHandling())
            .append("executeMode", getExecuteMode())
            .append("applyData", getApplyData())
            .append("runTime", getRunTime())
            .toString();
    }
}

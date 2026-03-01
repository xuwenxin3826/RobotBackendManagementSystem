package com.ruoyi.data.mapper;

import java.util.List;
import com.ruoyi.data.mapper.po.CleanRulePo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CleanRuleMapper {

    /**
     * 根据ID查询任务
     */
    CleanRulePo selectById(@Param("id") Long id);

    List<String> getTableColumns(@Param("tableName") String tableName);
}
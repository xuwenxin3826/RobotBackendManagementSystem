package com.ruoyi.data.mapper;

import com.ruoyi.data.domain.CleanExecuteHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CleanExecuteHistoryMapper {

    int insert(CleanExecuteHistory history);

    CleanExecuteHistory selectById(@Param("id") Long id);

    List<CleanExecuteHistory> selectAll();

    int updateStatus(@Param("id") Long id,
                     @Param("status") Integer status);

    int deleteById(@Param("id") Long id);
}
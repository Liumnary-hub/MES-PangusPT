package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.dto.SPCQueryDTO;
import com.mes.entity.QualityRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

// QualityRecordMapper.java
@Mapper
public interface QualityRecordMapper extends BaseMapper<QualityRecord> {

    /**
     * 根据任务ID查询检验记录
     */
    List<QualityRecord> selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据计划ID查询检验记录
     */
    List<QualityRecord> selectByPlanId(@Param("planId") Long planId);

    /**
     * 获取SPC分析数据
     */
    List<QualityRecord> getRecordsForSPC(@Param("query") SPCQueryDTO query);

    /**
     * 根据项目和日期范围查询记录
     */
    List<QualityRecord> selectByItemAndDateRange(@Param("itemId") Long itemId,
                                                 @Param("startDate") LocalDateTime startDate,
                                                 @Param("endDate") LocalDateTime endDate);

    /**
     * 批量插入检验记录
     */
    int insertBatch(@Param("records") List<QualityRecord> records);

    /**
     * 根据任务ID删除检验记录
     */
    int deleteByTaskId(@Param("taskId") Long taskId);
}

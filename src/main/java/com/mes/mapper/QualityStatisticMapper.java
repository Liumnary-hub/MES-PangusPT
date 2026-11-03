package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.dto.StatisticQueryDTO;
import com.mes.entity.QualityStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// QualityStatisticMapper.java
@Mapper
public interface QualityStatisticMapper extends BaseMapper<QualityStatistic> {

    /**
     * 根据日期查询统计
     */
    QualityStatistic selectByDate(@Param("statisticDate") String statisticDate,
                                  @Param("productCode") String productCode);

    /**
     * 查询统计列表
     */
    List<QualityStatistic> selectStatisticList(StatisticQueryDTO query);

    /**
     * 获取日期范围内的统计
     */
    List<QualityStatistic> selectByDateRange(@Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("productCode") String productCode);

    /**
     * 批量插入或更新统计
     */
    int upsertBatch(@Param("statistics") List<QualityStatistic> statistics);

    /**
     * 删除指定日期的统计
     */
    int deleteByDate(@Param("statisticDate") String statisticDate);
}
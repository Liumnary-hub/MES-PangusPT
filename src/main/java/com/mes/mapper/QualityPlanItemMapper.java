package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.QualityPlanItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

// QualityPlanItemMapper.java
@Mapper
public interface QualityPlanItemMapper extends BaseMapper<QualityPlanItem> {

    /**
     * 根据计划ID查询明细
     */
    List<QualityPlanItem> selectByPlanId(@Param("planId") Long planId);

    /**
     * 根据计划ID删除明细
     */
    int deleteByPlanId(@Param("planId") Long planId);

    /**
     * 批量插入明细
     */
    int insertBatch(@Param("items") List<QualityPlanItem> items);

    /**
     * 根据计划ID统计明细数量
     */
    Long countByPlanId(@Param("planId") Long planId);

    /**
     * 根据标准ID查询计划明细
     */
    List<QualityPlanItem> selectByStandardId(@Param("standardId") Long standardId);

    /**
     * 批量删除计划明细
     */
    int deleteBatchByPlanIds(@Param("planIds") List<Long> planIds);

    /**
     * 查询计划明细（带标准信息）
     */
    List<QualityPlanItem> selectPlanItemsWithStandard(@Param("planId") Long planId);
}
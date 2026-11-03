package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.dto.PlanQueryDTO;
import com.mes.entity.QualityPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 检验计划Mapper接口
 */
@Mapper
public interface QualityPlanMapper extends BaseMapper<QualityPlan> {

    /**
     * 分页查询检验计划列表
     */
    IPage<QualityPlan> selectPageList(Page<QualityPlan> page, @Param("query") PlanQueryDTO query);

    /**
     * 根据计划编号查询
     */
    QualityPlan selectByPlanNo(@Param("planNo") String planNo);

    /**
     * 根据产品ID查询检验计划
     */
    List<QualityPlan> selectByProductId(@Param("productId") Long productId);

    /**
     * 根据标准ID查询检验计划
     */
    List<QualityPlan> selectByStandardId(@Param("standardId") Long standardId);

    /**
     * 查询当天需要执行的计划
     */
    List<QualityPlan> selectTodayPlans();

    /**
     * 查询逾期计划
     */
    List<QualityPlan> selectOverduePlans();

    /**
     * 查询待处理计划
     */
    List<QualityPlan> selectPendingPlans();

    /**
     * 查询活跃计划（执行中）
     */
    List<QualityPlan> selectActivePlans();

    /**
     * 根据状态统计计划数量
     */
    Map<String, Long> countByStatus(@Param("query") PlanQueryDTO query);

    /**
     * 根据计划类型统计数量
     */
    Map<String, Long> countByPlanType(@Param("query") PlanQueryDTO query);

    /**
     * 获取月度趋势
     */
    List<Map<String, Object>> getMonthlyTrend(@Param("query") PlanQueryDTO query);

    /**
     * 获取产品统计
     */
    List<Map<String, Object>> getProductStatistics(@Param("query") PlanQueryDTO query);

    /**
     * 批量逻辑删除
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids);

    /**
     * 更新计划状态
     */
    int updatePlanStatus(@Param("id") Long id,
                         @Param("status") String status,
                         @Param("updateBy") String updateBy);

    /**
     * 批量更新计划状态
     */
    int updateBatchStatus(@Param("ids") List<Long> ids,
                          @Param("status") String status,
                          @Param("updateBy") String updateBy);
}
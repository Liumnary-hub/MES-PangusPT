package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.dto.TaskQueryDTO;
import com.mes.entity.QualityTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface QualityTaskMapper extends BaseMapper<QualityTask> {

    /**
     * 分页查询检验任务
     */
    IPage<QualityTask> selectPageList(Page<QualityTask> page, @Param("query") TaskQueryDTO query);

    /**
     * 查询待检任务
     */
    List<QualityTask> selectPendingTasks(@Param("inspector") String inspector);

    /**
     * 查询检验中的任务
     */
    List<QualityTask> selectInspectingTasks(@Param("inspector") String inspector);

    /**
     * 查询超时任务
     */
    List<QualityTask> selectTimeoutTasks(@Param("timeoutMinutes") Integer timeoutMinutes);

    /**
     * 根据计划ID查询任务
     */
    List<QualityTask> selectByPlanId(@Param("planId") Long planId);

    /**
     * 统计待检任务数量
     */
    Long countPendingTasksByPlan(@Param("planId") Long planId);

    /**
     * 获取今日统计
     */
    Map<String, Object> getTodayStatistics(@Param("today") String today);

    /**
     * 获取基础统计
     */
    Map<String, Object> getBasicStatistics(@Param("query") TaskQueryDTO query);

    /**
     * 获取合格率趋势
     */
    List<Map<String, Object>> getPassRateTrend(@Param("query") TaskQueryDTO query);

    /**
     * 获取产品合格率排行（原 getProductPassRateRank）
     */
    List<Map<String, Object>> getProductStatistics(@Param("query") TaskQueryDTO query);

    /**
     * 获取检验员绩效（原 getInspectorPerformance）
     */
    List<Map<String, Object>> getInspectorStatistics(@Param("query") TaskQueryDTO query);

    /**
     * 批量更新任务状态
     */
    int updateBatchStatus(@Param("ids") List<Long> ids,
                          @Param("status") String status,
                          @Param("inspector") String inspector,
                          @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量逻辑删除
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids);

    // ============ 新增的统计方法 ============

    /**
     * 按状态统计任务数量（实现类需要）
     */
    Map<String, Long> countByStatus(@Param("query") TaskQueryDTO query);

    /**
     * 按结果统计任务数量（实现类需要）
     */
    Map<String, Long> countByResult(@Param("query") TaskQueryDTO query);

    /**
     * 按检验员统计任务数量（实现类需要）
     */
    Map<String, Long> countByInspector(@Param("query") TaskQueryDTO query);

    /**
     * 获取每日任务趋势（实现类需要）
     */
    List<Map<String, Object>> getDailyTrend(@Param("query") TaskQueryDTO query);

    /**
     * 导出任务数据（实现类需要）
     */
    List<Map<String, Object>> exportByQuery(@Param("query") TaskQueryDTO query);

    /**
     * 批量插入任务
     */
    int insertBatch(@Param("list") List<QualityTask> tasks);

    // ============ 可选的其他查询方法 ============

    /**
     * 根据产品编码查询
     */
    List<QualityTask> selectByProductCode(@Param("productCode") String productCode);

    /**
     * 根据批次号查询
     */
    List<QualityTask> selectByBatchNo(@Param("batchNo") String batchNo);

    /**
     * 查询紧急任务
     */
    List<QualityTask> selectUrgentTasks();

    /**
     * 查询我的任务（待检+检验中）
     */
    List<QualityTask> selectMyTasks(@Param("inspector") String inspector);

    /**
     * 查询指定日期范围的任务
     */
    List<QualityTask> selectByDateRange(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        @Param("status") String status);

    /**
     * 查询需要预警的任务
     */
    List<QualityTask> selectAlertTasks(@Param("hoursBeforeDue") Integer hoursBeforeDue);

    /**
     * 更新任务进度
     */
    @Update("UPDATE quality_task SET progress = #{progress}, update_time = #{updateTime} WHERE id = #{id}")
    int updateProgress(@Param("id") Long id,
                       @Param("progress") Integer progress,
                       @Param("updateTime") LocalDateTime updateTime);

    /**
     * 更新检验结果
     */
    int updateResult(@Param("id") Long id,
                     @Param("result") String result,
                     @Param("quantityChecked") Integer quantityChecked,
                     @Param("quantityQualified") Integer quantityQualified,
                     @Param("quantityDefective") Integer quantityDefective,
                     @Param("remark") String remark,
                     @Param("updateTime") LocalDateTime updateTime);

    /**
     * 获取任务统计概览
     */
    Map<String, Object> getTaskOverview(@Param("inspector") String inspector,
                                        @Param("productCode") String productCode,
                                        @Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate);
}
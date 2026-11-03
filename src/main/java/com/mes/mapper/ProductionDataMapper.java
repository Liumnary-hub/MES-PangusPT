package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionData;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductionDataMapper extends BaseMapper<ProductionData> {

    /**
     * 根据任务ID和数据类型查询数据
     */
    @Select("SELECT * FROM mes_production_data WHERE task_id = #{taskId} AND data_type = #{dataType} AND is_deleted = 0 ORDER BY collection_time DESC")
    List<ProductionData> selectByTaskIdAndType(@Param("taskId") Long taskId, @Param("dataType") String dataType);

    /**
     * 获取指定时间段内的产量汇总
     */
    @Select("SELECT SUM(CAST(data_value AS DECIMAL)) as total_quantity, COUNT(*) as data_count " +
            "FROM mes_production_data " +
            "WHERE task_id = #{taskId} AND data_type = 'quantity' " +
            "AND collection_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0")
    Map<String, Object> selectProductionSummary(@Param("taskId") Long taskId,
                                                @Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);

    /**
     * 获取质量数据统计
     */
    @Select("SELECT data_value, COUNT(*) as count " +
            "FROM mes_production_data " +
            "WHERE task_id = #{taskId} AND data_type = 'quality' " +
            "AND collection_time BETWEEN #{startTime} AND #{endTime} AND is_deleted = 0 " +
            "GROUP BY data_value")
    List<Map<String, Object>> selectQualityStatistics(@Param("taskId") Long taskId,
                                                      @Param("startTime") LocalDateTime startTime,
                                                      @Param("endTime") LocalDateTime endTime);

    /**
     * 获取资源的最新数据
     */
    @Select("SELECT pd.* FROM mes_production_data pd " +
            "JOIN mes_production_task pt ON pd.task_id = pt.id " +
            "WHERE pt.resource_id = #{resourceId} AND pd.collection_time >= #{sinceTime} " +
            "AND pd.is_deleted = 0 AND pt.is_deleted = 0 " +
            "ORDER BY pd.collection_time DESC")
    List<ProductionData> selectRecentDataByResource(@Param("resourceId") Long resourceId,
                                                    @Param("sinceTime") LocalDateTime sinceTime);

    /**
     * 获取任务的最新数据记录
     */
    @Select("SELECT * FROM mes_production_data " +
            "WHERE task_id = #{taskId} AND data_type = #{dataType} AND is_deleted = 0 " +
            "ORDER BY collection_time DESC LIMIT 1")
    ProductionData selectLatestData(@Param("taskId") Long taskId, @Param("dataType") String dataType);

    /**
     * 批量插入生产数据
     */
    int batchInsert(@Param("list") List<ProductionData> dataList);

    /**
     * 根据条件分页查询（复杂查询使用XML）
     */
    Page<ProductionData> selectDataByCondition(Page<ProductionData> page,
                                               @Param("taskId") Long taskId,
                                               @Param("dataType") String dataType,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备状态统计数据
     */
    @MapKey("status")
    List<Map<String, Object>> selectEquipmentStatusStats(@Param("taskId") Long taskId,
                                                         @Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 获取生产数据趋势 - 返回List而不是Map
     */
    @MapKey("date")
    List<Map<String, Object>> selectProductionTrend(@Param("taskId") Long taskId,
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备运行效率统计 - 返回List而不是Map
     */
    @MapKey("date")
    List<Map<String, Object>> selectEquipmentEfficiencyStats(@Param("resourceId") Long resourceId,
                                                             @Param("startTime") LocalDateTime startTime,
                                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 获取生产异常统计 - 返回List而不是Map
     */
    @MapKey("anomaly_type")
    List<Map<String, Object>> selectProductionAnomalyStats(@Param("taskId") Long taskId,
                                                           @Param("startTime") LocalDateTime startTime,
                                                           @Param("endTime") LocalDateTime endTime);
}
package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductionTaskMapper extends BaseMapper<ProductionTask> {

    /**
     * 根据工单ID查询任务
     */
    @Select("SELECT * FROM mes_production_task WHERE order_id = #{orderId} AND is_deleted = 0 ORDER BY operation_sequence")
    List<ProductionTask> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据资源ID查询任务
     */
    @Select("SELECT * FROM mes_production_task WHERE resource_id = #{resourceId} AND is_deleted = 0 ORDER BY planned_start_time")
    List<ProductionTask> selectByResourceId(@Param("resourceId") Long resourceId);

    /**
     * 根据状态查询任务
     */
    List<ProductionTask> selectByStatus(@Param("status") String status);

    /**
     * 查询工位待执行任务
     */
    @Select("SELECT * FROM mes_production_task WHERE resource_id = #{resourceId} AND status = 'pending' AND is_deleted = 0 ORDER BY planned_start_time")
    List<ProductionTask> selectPendingTasksByResource(@Param("resourceId") Long resourceId);

    /**
     * 分页查询任务
     */
    Page<ProductionTask> selectTaskPage(Page<ProductionTask> page,
                                        @Param("taskCode") String taskCode,
                                        @Param("orderId") Long orderId,
                                        @Param("resourceId") Long resourceId,
                                        @Param("status") String status);

    /**
     * 更新任务状态
     */
    int updateTaskStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 更新任务进度
     */
    int updateTaskProgress(@Param("id") Long id,
                           @Param("completedQuantity") Integer completedQuantity,
                           @Param("qualifiedQuantity") Integer qualifiedQuantity,
                           @Param("actualTime") Double actualTime);




    @Select("SELECT * FROM mes_production_task WHERE order_id = #{orderId} AND status = #{status} AND is_deleted = 0 ORDER BY operation_sequence")
    List<ProductionTask> selectByOrderIdAndStatus(@Param("orderId") Long orderId, @Param("status") String status);

    /**
     * 统计工单任务状态
     */
    @Select("SELECT status, COUNT(*) as count FROM mes_production_task WHERE order_id = #{orderId} AND is_deleted = 0 GROUP BY status")
    List<Map<String, Object>> countTasksByStatus(@Param("orderId") Long orderId);

    /**
     * 批量更新任务状态
     */
    @Update("UPDATE mes_production_task SET status = #{status}, update_time = NOW() WHERE order_id = #{orderId} AND is_deleted = 0")
    int updateStatusByOrderId(@Param("orderId") Long orderId, @Param("status") String status);
}
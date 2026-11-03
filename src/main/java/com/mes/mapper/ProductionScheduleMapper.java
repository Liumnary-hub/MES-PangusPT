package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ProductionScheduleMapper extends BaseMapper<ProductionSchedule> {

    /**
     * 根据条件分页查询排产计划
     */
    IPage<ProductionSchedule> selectSchedulePage(Page<ProductionSchedule> page,
                                                 @Param("scheduleCode") String scheduleCode,
                                                 @Param("scheduleName") String scheduleName,
                                                 @Param("scheduleDate") LocalDate scheduleDate,
                                                 @Param("scheduleType") String scheduleType,
                                                 @Param("status") String status);

    /**
     * 根据日期范围查询排产计划
     */
    List<ProductionSchedule> selectByDateRange(@Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    /**
     * 根据状态查询排产计划
     */
    List<ProductionSchedule> selectByStatus(@Param("status") String status);

    /**
     * 更新排产计划状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 检查排产计划编号是否存在
     */
    int countByScheduleCode(@Param("scheduleCode") String scheduleCode,
                            @Param("excludeId") Long excludeId);

    /**
     * 根据工单ID查询相关的排产计划
     */
    List<ProductionSchedule> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 获取最新的排产计划
     */
    ProductionSchedule selectLatestSchedule();
}
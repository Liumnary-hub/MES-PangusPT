package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProductionSchedule;

import java.time.LocalDate;
import java.util.List;

public interface ProductionScheduleService extends IService<ProductionSchedule> {

    /**
     * 分页查询排产计划
     */
    IPage<ProductionSchedule> getSchedulePage(Page<ProductionSchedule> page,
                                              String scheduleCode,
                                              String scheduleName,
                                              LocalDate scheduleDate,
                                              String scheduleType,
                                              String status);

    /**
     * 创建排产计划
     */
    boolean createSchedule(ProductionSchedule schedule);

    /**
     * 更新排产计划状态
     */
    boolean updateScheduleStatus(Long id, String status);

    /**
     * 发布排产计划
     */
    boolean publishSchedule(Long id);

    /**
     * 取消排产计划
     */
    boolean cancelSchedule(Long id);

    /**
     * 根据日期范围查询排产计划
     */
    List<ProductionSchedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 检查排产计划编号是否重复
     */
    boolean isScheduleCodeDuplicate(String scheduleCode, Long excludeId);

    /**
     * 获取最新的排产计划
     */
    ProductionSchedule getLatestSchedule();

    /**
     * 根据工单ID查询排产计划
     */
    List<ProductionSchedule> getSchedulesByOrderId(Long orderId);
}
package com.mes.service;

import java.time.LocalDateTime;
import java.util.Map;

public interface ProductionPerformanceService {

    /**
     * 计算OEE
     */
    Map<String, Object> calculateOEE(Long resourceId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取工单绩效
     */
    Map<String, Object> getOrderPerformance(Long orderId);

    /**
     * 获取资源绩效
     */
    Map<String, Object> getResourcePerformance(Long resourceId, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取部门绩效
     */
    Map<String, Object> getDepartmentPerformance(String department, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取仪表盘数据
     */
    Map<String, Object> getDashboardData();

    /**
     * 计算生产效率
     */
    Map<String, Object> calculateProductionEfficiency(LocalDateTime startTime, LocalDateTime endTime);
}
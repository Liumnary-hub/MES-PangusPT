package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProcessStep;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ProcessStepService extends IService<ProcessStep> {

    /**
     * 条件分页查询工序
     */
    IPage<ProcessStep> getStepsByCondition(Page<ProcessStep> page, String stepCode, String stepName, Long routeId);

    /**
     * 根据工艺路线ID查询工序
     */
    List<ProcessStep> getStepsByRouteId(Long routeId);

    /**
     * 更新工序顺序
     */
    boolean updateStepSequence(Long routeId, List<Long> stepIds);

    /**
     * 根据资源类型查询工序
     */
    List<ProcessStep> getStepsByResourceType(String resourceType);

    /**
     * 复制工序到新工艺路线
     */
    boolean copyStepsToRoute(Long sourceRouteId, Long targetRouteId);

    Map<String, Object> getDashboardData();

    Map<String, Object> getDepartmentPerformance(String department, LocalDateTime startTime, LocalDateTime endTime);

    Map<String, Object> getResourcePerformance(Long resourceId, LocalDateTime startTime, LocalDateTime endTime);

    Map<String, Object> getOrderPerformance(Long orderId);

    Map<String, Object> calculateOEE(Long resourceId, LocalDateTime startTime, LocalDateTime endTime);
}
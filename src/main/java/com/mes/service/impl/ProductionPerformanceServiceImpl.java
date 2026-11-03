package com.mes.service.impl;

import com.mes.entity.ProductionOrder;
import com.mes.entity.ProductionTask;
import com.mes.entity.ProductionResource;
import com.mes.service.ProductionOrderService;
import com.mes.service.ProductionTaskService;
import com.mes.service.ProductionResourceService;
import com.mes.service.ProductionDataService;
import com.mes.service.ProductionPerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductionPerformanceServiceImpl implements ProductionPerformanceService {

    @Autowired
    private ProductionOrderService orderService;

    @Autowired
    private ProductionTaskService taskService;

    @Autowired
    private ProductionResourceService resourceService;

    @Autowired
    private ProductionDataService dataService;

    @Override
    public Map<String, Object> calculateOEE(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        // OEE = 可用率 × 表现性 × 质量指数

        // 1. 计算可用率 (Availability)
        double availability = calculateAvailability(resourceId, startTime, endTime);

        // 2. 计算表现性 (Performance)
        double performance = calculatePerformance(resourceId, startTime, endTime);

        // 3. 计算质量指数 (Quality)
        double quality = calculateQuality(resourceId, startTime, endTime);

        // 4. 计算总体OEE
        double oee = availability * performance * quality * 100; // 转换为百分比

        Map<String, Object> oeeData = new HashMap<>();
        oeeData.put("availability", Math.round(availability * 10000.0) / 100.0);
        oeeData.put("performance", Math.round(performance * 10000.0) / 100.0);
        oeeData.put("quality", Math.round(quality * 10000.0) / 100.0);
        oeeData.put("oee", Math.round(oee * 100.0) / 100.0);
        oeeData.put("resourceId", resourceId);
        oeeData.put("startTime", startTime);
        oeeData.put("endTime", endTime);

        return oeeData;
    }

    @Override
    public Map<String, Object> getOrderPerformance(Long orderId) {
        ProductionOrder order = orderService.getById(orderId);
        if (order == null) {
            return new HashMap<>();
        }

        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getOrderId, orderId)
                .list();

        // 计算订单绩效指标
        int totalPlannedQuantity = order.getPlannedQuantity();
        int totalCompletedQuantity = order.getCompletedQuantity();
        double completionRate = totalPlannedQuantity > 0 ?
                (double) totalCompletedQuantity / totalPlannedQuantity * 100 : 0;

        // 计算总标准工时和总实际工时
        double totalStandardTime = tasks.stream()
                .mapToDouble(ProductionTask::getStandardTime)
                .sum();

        double totalActualTime = tasks.stream()
                .mapToDouble(task -> task.getActualTime() != null ? task.getActualTime() : 0)
                .sum();

        double efficiency = totalStandardTime > 0 ?
                totalStandardTime / totalActualTime * 100 : 0;

        // 计算合格率
        int totalQualified = tasks.stream()
                .mapToInt(ProductionTask::getQualifiedQuantity)
                .sum();

        int totalProduced = tasks.stream()
                .mapToInt(ProductionTask::getCompletedQuantity)
                .sum();

        double qualityRate = totalProduced > 0 ?
                (double) totalQualified / totalProduced * 100 : 0;

        Map<String, Object> performance = new HashMap<>();
        performance.put("orderCode", order.getOrderCode());
        performance.put("productCode", order.getProductCode());
        performance.put("plannedQuantity", totalPlannedQuantity);
        performance.put("completedQuantity", totalCompletedQuantity);
        performance.put("completionRate", Math.round(completionRate * 100.0) / 100.0);
        performance.put("totalStandardTime", Math.round(totalStandardTime * 100.0) / 100.0);
        performance.put("totalActualTime", Math.round(totalActualTime * 100.0) / 100.0);
        performance.put("efficiency", Math.round(efficiency * 100.0) / 100.0);
        performance.put("qualityRate", Math.round(qualityRate * 100.0) / 100.0);
        performance.put("taskCount", tasks.size());
        performance.put("completedTaskCount",
                tasks.stream().filter(t -> "completed".equals(t.getStatus())).count());

        return performance;
    }

    @Override
    public Map<String, Object> getResourcePerformance(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        ProductionResource resource = resourceService.getById(resourceId);
        if (resource == null) {
            return new HashMap<>();
        }

        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getResourceId, resourceId)
                .between(ProductionTask::getActualStartTime, startTime, endTime)
                .list();

        // 计算资源利用率
        long totalOperatingTime = tasks.stream()
                .filter(task -> task.getActualStartTime() != null && task.getActualEndTime() != null)
                .mapToLong(task -> Duration.between(task.getActualStartTime(), task.getActualEndTime()).toMinutes())
                .sum();

        long totalAvailableTime = Duration.between(startTime, endTime).toMinutes();
        double utilizationRate = totalAvailableTime > 0 ?
                (double) totalOperatingTime / totalAvailableTime * 100 : 0;

        // 计算平均效率
        double avgEfficiency = tasks.stream()
                .filter(task -> task.getStandardTime() != null && task.getActualTime() != null && task.getActualTime() > 0)
                .mapToDouble(task -> task.getStandardTime() / task.getActualTime() * 100)
                .average()
                .orElse(0);

        // 计算总产量和合格率
        int totalOutput = tasks.stream()
                .mapToInt(ProductionTask::getCompletedQuantity)
                .sum();

        int totalQualified = tasks.stream()
                .mapToInt(ProductionTask::getQualifiedQuantity)
                .sum();

        double qualityRate = totalOutput > 0 ? (double) totalQualified / totalOutput * 100 : 0;

        Map<String, Object> performance = new HashMap<>();
        performance.put("resourceCode", resource.getResourceCode());
        performance.put("resourceName", resource.getResourceName());
        performance.put("resourceType", resource.getResourceType());
        performance.put("totalOperatingTime", totalOperatingTime);
        performance.put("totalAvailableTime", totalAvailableTime);
        performance.put("utilizationRate", Math.round(utilizationRate * 100.0) / 100.0);
        performance.put("avgEfficiency", Math.round(avgEfficiency * 100.0) / 100.0);
        performance.put("totalOutput", totalOutput);
        performance.put("totalQualified", totalQualified);
        performance.put("qualityRate", Math.round(qualityRate * 100.0) / 100.0);
        performance.put("taskCount", tasks.size());
        performance.put("startTime", startTime);
        performance.put("endTime", endTime);

        return performance;
    }

    @Override
    public Map<String, Object> getDepartmentPerformance(String department, LocalDateTime startTime, LocalDateTime endTime) {
        // 获取部门所有资源
        List<ProductionResource> departmentResources = resourceService.lambdaQuery()
                .eq(ProductionResource::getDepartment, department)
                .list();

        // 计算部门整体指标
        int totalResources = departmentResources.size();
        int activeResources = (int) departmentResources.stream()
                .filter(res -> "active".equals(res.getStatus()))
                .count();

        // 计算部门总产量和效率
        double totalOutput = 0;
        double totalUtilization = 0;
        double totalEfficiency = 0;
        int resourceCount = 0;

        for (ProductionResource resource : departmentResources) {
            Map<String, Object> resourcePerf = getResourcePerformance(resource.getId(), startTime, endTime);
            if (!resourcePerf.isEmpty()) {
                totalOutput += (int) resourcePerf.get("totalOutput");
                totalUtilization += (double) resourcePerf.get("utilizationRate");
                totalEfficiency += (double) resourcePerf.get("avgEfficiency");
                resourceCount++;
            }
        }

        double avgUtilization = resourceCount > 0 ? totalUtilization / resourceCount : 0;
        double avgEfficiency = resourceCount > 0 ? totalEfficiency / resourceCount : 0;

        Map<String, Object> performance = new HashMap<>();
        performance.put("department", department);
        performance.put("totalResources", totalResources);
        performance.put("activeResources", activeResources);
        performance.put("resourceUtilizationRate", Math.round(avgUtilization * 100.0) / 100.0);
        performance.put("resourceEfficiency", Math.round(avgEfficiency * 100.0) / 100.0);
        performance.put("totalOutput", totalOutput);
        performance.put("startTime", startTime);
        performance.put("endTime", endTime);

        return performance;
    }

    @Override
    public Map<String, Object> getDashboardData() {
        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime todayEnd = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

        Map<String, Object> dashboard = new HashMap<>();

        // 今日工单统计
        List<ProductionOrder> todayOrders = orderService.lambdaQuery()
                .ge(ProductionOrder::getCreateTime, todayStart)
                .le(ProductionOrder::getCreateTime, todayEnd)
                .list();

        dashboard.put("todayOrderCount", todayOrders.size());
        dashboard.put("todayCompletedOrders",
                todayOrders.stream().filter(o -> "completed".equals(o.getStatus())).count());

        // 今日任务统计
        List<ProductionTask> todayTasks = taskService.lambdaQuery()
                .ge(ProductionTask::getCreateTime, todayStart)
                .le(ProductionTask::getCreateTime, todayEnd)
                .list();

        dashboard.put("todayTaskCount", todayTasks.size());
        dashboard.put("todayCompletedTasks",
                todayTasks.stream().filter(t -> "completed".equals(t.getStatus())).count());

        // 资源状态统计
        List<ProductionResource> allResources = resourceService.list();
        dashboard.put("totalResources", allResources.size());
        dashboard.put("activeResources",
                allResources.stream().filter(r -> "active".equals(r.getStatus())).count());

        // 今日产量统计
        int todayOutput = todayTasks.stream()
                .mapToInt(ProductionTask::getCompletedQuantity)
                .sum();
        dashboard.put("todayOutput", todayOutput);

        return dashboard;
    }

    @Override
    public Map<String, Object> calculateProductionEfficiency(LocalDateTime startTime, LocalDateTime endTime) {
        List<ProductionTask> tasks = taskService.lambdaQuery()
                .between(ProductionTask::getActualStartTime, startTime, endTime)
                .list();

        // 计算整体效率指标
        double totalStandardTime = tasks.stream()
                .filter(task -> task.getStandardTime() != null)
                .mapToDouble(ProductionTask::getStandardTime)
                .sum();

        double totalActualTime = tasks.stream()
                .filter(task -> task.getActualTime() != null)
                .mapToDouble(ProductionTask::getActualTime)
                .sum();

        double overallEfficiency = totalStandardTime > 0 ?
                totalStandardTime / totalActualTime * 100 : 0;

        // 计算计划达成率
        List<ProductionOrder> orders = orderService.lambdaQuery()
                .between(ProductionOrder::getPlannedStartTime, startTime, endTime)
                .list();

        int totalPlanned = orders.stream()
                .mapToInt(ProductionOrder::getPlannedQuantity)
                .sum();

        int totalCompleted = orders.stream()
                .mapToInt(ProductionOrder::getCompletedQuantity)
                .sum();

        double planAchievementRate = totalPlanned > 0 ?
                (double) totalCompleted / totalPlanned * 100 : 0;

        Map<String, Object> efficiency = new HashMap<>();
        efficiency.put("overallEfficiency", Math.round(overallEfficiency * 100.0) / 100.0);
        efficiency.put("planAchievementRate", Math.round(planAchievementRate * 100.0) / 100.0);
        efficiency.put("totalStandardTime", Math.round(totalStandardTime * 100.0) / 100.0);
        efficiency.put("totalActualTime", Math.round(totalActualTime * 100.0) / 100.0);
        efficiency.put("taskCount", tasks.size());
        efficiency.put("orderCount", orders.size());
        efficiency.put("startTime", startTime);
        efficiency.put("endTime", endTime);

        return efficiency;
    }

    // ========== 私有方法 ==========

    private double calculateAvailability(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        // 可用率 = 运行时间 / 计划运行时间
        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getResourceId, resourceId)
                .between(ProductionTask::getActualStartTime, startTime, endTime)
                .list();

        long totalRunningTime = tasks.stream()
                .filter(task -> task.getActualStartTime() != null && task.getActualEndTime() != null)
                .mapToLong(task -> Duration.between(task.getActualStartTime(), task.getActualEndTime()).toMinutes())
                .sum();

        long totalPlannedTime = Duration.between(startTime, endTime).toMinutes();

        return totalPlannedTime > 0 ? (double) totalRunningTime / totalPlannedTime : 0;
    }

    private double calculatePerformance(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        // 表现性 = (总产量 × 标准周期时间) / 运行时间
        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getResourceId, resourceId)
                .between(ProductionTask::getActualStartTime, startTime, endTime)
                .list();

        double totalStandardOutput = tasks.stream()
                .filter(task -> task.getStandardTime() != null && task.getPlannedQuantity() != null)
                .mapToDouble(task -> task.getStandardTime() * task.getPlannedQuantity())
                .sum();

        long totalRunningTime = tasks.stream()
                .filter(task -> task.getActualStartTime() != null && task.getActualEndTime() != null)
                .mapToLong(task -> Duration.between(task.getActualStartTime(), task.getActualEndTime()).toMinutes())
                .sum();

        // 转换为小时
        double runningTimeHours = totalRunningTime / 60.0;

        return runningTimeHours > 0 ? totalStandardOutput / runningTimeHours : 0;
    }

    private double calculateQuality(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        // 质量指数 = 合格数量 / 总生产数量
        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getResourceId, resourceId)
                .between(ProductionTask::getActualStartTime, startTime, endTime)
                .list();

        int totalQualified = tasks.stream()
                .mapToInt(ProductionTask::getQualifiedQuantity)
                .sum();

        int totalProduced = tasks.stream()
                .mapToInt(ProductionTask::getCompletedQuantity)
                .sum();

        return totalProduced > 0 ? (double) totalQualified / totalProduced : 0;
    }
}
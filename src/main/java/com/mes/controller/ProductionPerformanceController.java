package com.mes.controller;

import com.mes.service.ProcessStepService;
import com.mes.service.ProductionPerformanceService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/production/performance")
public class ProductionPerformanceController {

    @Autowired
    private ProcessStepService performanceService;

    @GetMapping("/oee/{resourceId}")
    public Result<Map<String, Object>> getOEE(@PathVariable Long resourceId,
                                              @RequestParam LocalDateTime startTime,
                                              @RequestParam LocalDateTime endTime) {
        Map<String, Object> oeeData = performanceService.calculateOEE(resourceId, startTime, endTime);
        return Result.success(oeeData);
    }

    @GetMapping("/order/{orderId}")
    public Result<Map<String, Object>> getOrderPerformance(@PathVariable Long orderId) {
        Map<String, Object> performance = performanceService.getOrderPerformance(orderId);
        return Result.success(performance);
    }

    @GetMapping("/resource/{resourceId}")
    public Result<Map<String, Object>> getResourcePerformance(@PathVariable Long resourceId,
                                                              @RequestParam LocalDateTime startTime,
                                                              @RequestParam LocalDateTime endTime) {
        Map<String, Object> performance = performanceService.getResourcePerformance(resourceId, startTime, endTime);
        return Result.success(performance);
    }

    @GetMapping("/department/{department}")
    public Result<Map<String, Object>> getDepartmentPerformance(@PathVariable String department,
                                                                @RequestParam LocalDateTime startTime,
                                                                @RequestParam LocalDateTime endTime) {
        Map<String, Object> performance = performanceService.getDepartmentPerformance(department, startTime, endTime);
        return Result.success(performance);
    }

    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardData() {
        Map<String, Object> dashboardData = performanceService.getDashboardData();
        return Result.success(dashboardData);
    }
}
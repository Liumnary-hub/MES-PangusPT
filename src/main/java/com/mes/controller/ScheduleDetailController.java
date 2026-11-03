package com.mes.controller;

import com.mes.entity.ScheduleDetail;
import com.mes.service.ScheduleDetailService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/scheduling/details")
public class ScheduleDetailController {

    @Autowired
    private ScheduleDetailService scheduleDetailService;

    @GetMapping("/schedule/{scheduleId}")
    public Result<List<ScheduleDetail>> getDetailsByScheduleId(@PathVariable Long scheduleId) {
        try {
            List<ScheduleDetail> details = scheduleDetailService.getDetailsByScheduleId(scheduleId);
            return Result.success(details);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public Result<List<ScheduleDetail>> getDetailsByOrderId(@PathVariable Long orderId) {
        try {
            List<ScheduleDetail> details = scheduleDetailService.getDetailsByOrderId(orderId);
            return Result.success(details);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/task/{taskId}")
    public Result<ScheduleDetail> getDetailByTaskId(@PathVariable Long taskId) {
        try {
            ScheduleDetail detail = scheduleDetailService.getDetailByTaskId(taskId);
            if (detail == null) {
                return Result.error("排产明细不存在");
            }
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/batch")
    public Result<Boolean> batchSaveDetails(@RequestBody List<ScheduleDetail> details) {
        try {
            boolean success = scheduleDetailService.batchSaveDetails(details);
            return success ? Result.success(true, "保存成功") : Result.error("保存失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/check-conflict")
    public Result<Boolean> checkResourceConflict(
            @RequestParam Long resourceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(required = false) Long excludeDetailId) {

        try {
            boolean hasConflict = scheduleDetailService.checkResourceConflict(resourceId, startTime, endTime, excludeDetailId);
            return Result.success(hasConflict);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateDetailStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean success = scheduleDetailService.updateDetailStatus(id, status);
            return success ? Result.success(true, "状态更新成功") : Result.error("状态更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/start-time")
    public Result<Boolean> updateActualStartTime(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime actualStartTime) {

        try {
            boolean success = scheduleDetailService.updateActualStartTime(id, actualStartTime);
            return success ? Result.success(true, "开始时间更新成功") : Result.error("开始时间更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/end-time")
    public Result<Boolean> updateActualEndTime(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime actualEndTime) {

        try {
            boolean success = scheduleDetailService.updateActualEndTime(id, actualEndTime);
            return success ? Result.success(true, "结束时间更新成功") : Result.error("结束时间更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/workload")
    public Result<List<ScheduleDetail>> getResourceWorkload(
            @RequestParam Long resourceId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {

        try {
            List<ScheduleDetail> workload = scheduleDetailService.getResourceWorkload(resourceId, startDate, endDate);
            return Result.success(workload);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
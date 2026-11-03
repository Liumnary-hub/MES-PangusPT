package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionSchedule;
import com.mes.service.ProductionScheduleService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/scheduling/schedules")
public class ProductionScheduleController {

    @Autowired
    private ProductionScheduleService productionScheduleService;

    @GetMapping
    public Result<IPage<ProductionSchedule>> getSchedules(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String scheduleCode,
            @RequestParam(required = false) String scheduleName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate scheduleDate,
            @RequestParam(required = false) String scheduleType,
            @RequestParam(required = false) String status) {

        try {
            Page<ProductionSchedule> page = new Page<>(pageNum, pageSize);
            IPage<ProductionSchedule> result = productionScheduleService.getSchedulePage(
                    page, scheduleCode, scheduleName, scheduleDate, scheduleType, status);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<ProductionSchedule> getScheduleById(@PathVariable Long id) {
        try {
            ProductionSchedule schedule = productionScheduleService.getById(id);
            if (schedule == null) {
                return Result.error("排产计划不存在");
            }
            return Result.success(schedule);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result<Boolean> createSchedule(@RequestBody ProductionSchedule schedule) {
        try {
            // 设置默认值
            if (schedule.getStatus() == null) {
                schedule.setStatus("DRAFT");
            }
            if (schedule.getTotalOrders() == null) {
                schedule.setTotalOrders(0);
            }
            if (schedule.getTotalTasks() == null) {
                schedule.setTotalTasks(0);
            }

            boolean success = productionScheduleService.createSchedule(schedule);
            return success ? Result.success(true, "创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateSchedule(@PathVariable Long id, @RequestBody ProductionSchedule schedule) {
        try {
            // 获取原有排产计划，检查状态
            ProductionSchedule existingSchedule = productionScheduleService.getById(id);
            if (existingSchedule == null) {
                return Result.error("排产计划不存在");
            }

            // 只有草稿状态的排产计划可以编辑
            if (!"DRAFT".equals(existingSchedule.getStatus())) {
                return Result.error("只有草稿状态的排产计划可以编辑");
            }

            schedule.setId(id);
            boolean success = productionScheduleService.updateById(schedule);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/publish")
    public Result<Boolean> publishSchedule(@PathVariable Long id) {
        try {
            boolean success = productionScheduleService.publishSchedule(id);
            return success ? Result.success(true, "发布成功") : Result.error("发布失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public Result<Boolean> cancelSchedule(@PathVariable Long id) {
        try {
            boolean success = productionScheduleService.cancelSchedule(id);
            return success ? Result.success(true, "取消成功") : Result.error("取消失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteSchedule(@PathVariable Long id) {
        try {
            // 获取排产计划，检查状态
            ProductionSchedule schedule = productionScheduleService.getById(id);
            if (schedule == null) {
                return Result.error("排产计划不存在");
            }

            // 只有草稿状态的排产计划可以删除
            if (!"DRAFT".equals(schedule.getStatus())) {
                return Result.error("只有草稿状态的排产计划可以删除");
            }

            boolean success = productionScheduleService.removeById(id);
            return success ? Result.success(true, "删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/date-range")
    public Result<List<ProductionSchedule>> getSchedulesByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        try {
            List<ProductionSchedule> schedules = productionScheduleService.getSchedulesByDateRange(startDate, endDate);
            return Result.success(schedules);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public Result<ProductionSchedule> getLatestSchedule() {
        try {
            ProductionSchedule schedule = productionScheduleService.getLatestSchedule();
            return Result.success(schedule);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/order/{orderId}")
    public Result<List<ProductionSchedule>> getSchedulesByOrderId(@PathVariable Long orderId) {
        try {
            List<ProductionSchedule> schedules = productionScheduleService.getSchedulesByOrderId(orderId);
            return Result.success(schedules);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
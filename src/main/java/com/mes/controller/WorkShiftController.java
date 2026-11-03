package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.WorkShift;
import com.mes.service.WorkShiftService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scheduling/shifts")
public class WorkShiftController {

    @Autowired
    private WorkShiftService workShiftService;

    @GetMapping
    public Result<IPage<WorkShift>> getShifts(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String shiftCode,
            @RequestParam(required = false) String shiftName,
            @RequestParam(required = false) Boolean isActive) {

        try {
            Page<WorkShift> page = new Page<>(pageNum, pageSize);
            IPage<WorkShift> result = workShiftService.getShiftPage(page, shiftCode, shiftName, isActive);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<WorkShift> getShiftById(@PathVariable Long id) {
        try {
            WorkShift shift = workShiftService.getById(id);
            if (shift == null) {
                return Result.error("班次不存在");
            }
            return Result.success(shift);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/active")
    public Result<List<WorkShift>> getActiveShifts() {
        try {
            List<WorkShift> shifts = workShiftService.getActiveShifts();
            return Result.success(shifts);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/code/{shiftCode}")
    public Result<WorkShift> getShiftByCode(@PathVariable String shiftCode) {
        try {
            WorkShift shift = workShiftService.getShiftByCode(shiftCode);
            if (shift == null) {
                return Result.error("班次不存在");
            }
            return Result.success(shift);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping
    public Result<Boolean> createShift(@RequestBody WorkShift shift) {
        try {
            // 设置默认值
            if (shift.getIsActive() == null) {
                shift.setIsActive(true);
            }

            boolean success = workShiftService.save(shift);
            return success ? Result.success(true, "创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateShift(@PathVariable Long id, @RequestBody WorkShift shift) {
        try {
            shift.setId(id);
            boolean success = workShiftService.updateById(shift);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateShiftStatus(@PathVariable Long id, @RequestParam Boolean isActive) {
        try {
            boolean success = workShiftService.updateShiftStatus(id, isActive);
            return success ? Result.success(true, "状态更新成功") : Result.error("状态更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteShift(@PathVariable Long id) {
        try {
            boolean success = workShiftService.removeById(id);
            return success ? Result.success(true, "删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/default")
    public Result<WorkShift> getDefaultShift() {
        try {
            WorkShift shift = workShiftService.getDefaultShift();
            return Result.success(shift);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
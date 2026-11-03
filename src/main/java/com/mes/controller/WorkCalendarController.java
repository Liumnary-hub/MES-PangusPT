package com.mes.controller;

import com.mes.entity.WorkCalendar;
import com.mes.service.WorkCalendarService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/scheduling/calendar")
public class WorkCalendarController {

    @Autowired
    private WorkCalendarService workCalendarService;

    @GetMapping
    public Result<List<WorkCalendar>> getCalendarByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        try {
            List<WorkCalendar> calendars = workCalendarService.getCalendarByDateRange(startDate, endDate);
            return Result.success(calendars);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/date/{date}")
    public Result<WorkCalendar> getCalendarByDate(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            WorkCalendar calendar = workCalendarService.getCalendarByDate(date);
            if (calendar == null) {
                return Result.error("工作日历不存在");
            }
            return Result.success(calendar);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/batch")
    public Result<Boolean> batchSaveCalendars(@RequestBody List<WorkCalendar> calendars) {
        try {
            boolean success = workCalendarService.batchSaveCalendars(calendars);
            return success ? Result.success(true, "保存成功") : Result.error("保存失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/day-type")
    public Result<List<WorkCalendar>> getCalendarByDayType(
            @RequestParam String dayType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        try {
            List<WorkCalendar> calendars = workCalendarService.getCalendarByDayType(dayType, startDate, endDate);
            return Result.success(calendars);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/working-days")
    public Result<List<LocalDate>> getWorkingDays(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        try {
            List<LocalDate> workingDays = workCalendarService.getWorkingDays(startDate, endDate);
            return Result.success(workingDays);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/non-working-days")
    public Result<List<LocalDate>> getNonWorkingDays(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        try {
            List<LocalDate> nonWorkingDays = workCalendarService.getNonWorkingDays(startDate, endDate);
            return Result.success(nonWorkingDays);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/init-month")
    public Result<Boolean> initMonthCalendar(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam(required = false) Long workShiftId) {

        try {
            boolean success = workCalendarService.initMonthCalendar(year, month, workShiftId);
            return success ? Result.success(true, "初始化成功") : Result.error("初始化失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateCalendar(@PathVariable Long id, @RequestBody WorkCalendar calendar) {
        try {
            calendar.setId(id);
            boolean success = workCalendarService.updateById(calendar);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteCalendar(@PathVariable Long id) {
        try {
            boolean success = workCalendarService.removeById(id);
            return success ? Result.success(true, "删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
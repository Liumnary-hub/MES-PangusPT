package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.WorkCalendar;

import java.time.LocalDate;
import java.util.List;

public interface WorkCalendarService extends IService<WorkCalendar> {

    /**
     * 根据日期范围查询工作日历
     */
    List<WorkCalendar> getCalendarByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 根据日期查询工作日历
     */
    WorkCalendar getCalendarByDate(LocalDate date);

    /**
     * 批量保存工作日历
     */
    boolean batchSaveCalendars(List<WorkCalendar> calendars);

    /**
     * 根据日期类型查询
     */
    List<WorkCalendar> getCalendarByDayType(String dayType, LocalDate startDate, LocalDate endDate);

    /**
     * 检查日期是否存在
     */
    boolean existsByDate(LocalDate date);

    /**
     * 根据班次ID查询工作日历
     */
    List<WorkCalendar> getCalendarByWorkShiftId(Long workShiftId);

    /**
     * 获取工作日列表
     */
    List<LocalDate> getWorkingDays(LocalDate startDate, LocalDate endDate);

    /**
     * 获取非工作日列表
     */
    List<LocalDate> getNonWorkingDays(LocalDate startDate, LocalDate endDate);

    /**
     * 初始化月份工作日历
     */
    boolean initMonthCalendar(int year, int month, Long workShiftId);
}
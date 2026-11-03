package com.mes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.WorkCalendar;
import com.mes.mapper.WorkCalendarMapper;
import com.mes.service.WorkCalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class WorkCalendarServiceImpl extends ServiceImpl<WorkCalendarMapper, WorkCalendar>
        implements WorkCalendarService {

    @Autowired
    private WorkCalendarMapper workCalendarMapper;

    @Override
    public List<WorkCalendar> getCalendarByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("根据日期范围查询工作日历, startDate: {}, endDate: {}", startDate, endDate);
        return workCalendarMapper.selectByDateRange(startDate, endDate);
    }

    @Override
    public WorkCalendar getCalendarByDate(LocalDate date) {
        log.debug("根据日期查询工作日历, date: {}", date);
        return workCalendarMapper.selectByDate(date);
    }

    @Override
    @Transactional
    public boolean batchSaveCalendars(List<WorkCalendar> calendars) {
        log.info("批量保存工作日历, 数量: {}", calendars.size());

        try {
            if (calendars.isEmpty()) {
                return true;
            }

            int result = workCalendarMapper.batchInsertOrUpdate(calendars);
            log.info("批量保存工作日历成功, 数量: {}", result);
            return true;

        } catch (Exception e) {
            log.error("批量保存工作日历失败", e);
            throw new RuntimeException("批量保存工作日历失败: " + e.getMessage());
        }
    }

    @Override
    public List<WorkCalendar> getCalendarByDayType(String dayType, LocalDate startDate, LocalDate endDate) {
        log.debug("根据日期类型查询工作日历, dayType: {}, startDate: {}, endDate: {}", dayType, startDate, endDate);
        return workCalendarMapper.selectByDayType(dayType, startDate, endDate);
    }

    @Override
    public boolean existsByDate(LocalDate date) {
        int count = workCalendarMapper.existsByDate(date);
        return count > 0;
    }

    @Override
    public List<WorkCalendar> getCalendarByWorkShiftId(Long workShiftId) {
        log.debug("根据班次ID查询工作日历, workShiftId: {}", workShiftId);
        return workCalendarMapper.selectByWorkShiftId(workShiftId);
    }

    @Override
    public List<LocalDate> getWorkingDays(LocalDate startDate, LocalDate endDate) {
        log.debug("获取工作日列表, startDate: {}, endDate: {}", startDate, endDate);
        return workCalendarMapper.getWorkingDays(startDate, endDate);
    }

    @Override
    public List<LocalDate> getNonWorkingDays(LocalDate startDate, LocalDate endDate) {
        log.debug("获取非工作日列表, startDate: {}, endDate: {}", startDate, endDate);
        return workCalendarMapper.getNonWorkingDays(startDate, endDate);
    }

    @Override
    @Transactional
    public boolean initMonthCalendar(int year, int month, Long workShiftId) {
        log.info("初始化月份工作日历, year: {}, month: {}, workShiftId: {}", year, month, workShiftId);

        try {
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDate startDate = yearMonth.atDay(1);
            LocalDate endDate = yearMonth.atEndOfMonth();

            List<WorkCalendar> calendars = new ArrayList<>();

            // 生成该月所有日期的日历
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                WorkCalendar calendar = new WorkCalendar();
                calendar.setCalendarDate(date);
                calendar.setWorkShiftId(workShiftId);

                // 判断日期类型
                DayOfWeek dayOfWeek = date.getDayOfWeek();
                if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                    calendar.setDayType("WEEKEND");
                    calendar.setCapacityFactor(BigDecimal.valueOf(0.0));
                } else {
                    calendar.setDayType("WORKDAY");
                    calendar.setCapacityFactor(BigDecimal.valueOf(1.0));
                }

                calendar.setCreateTime(LocalDateTime.now());
                calendar.setUpdateTime(LocalDateTime.now());
                calendars.add(calendar);
            }

            return batchSaveCalendars(calendars);

        } catch (Exception e) {
            log.error("初始化月份工作日历失败, year: {}, month: {}", year, month, e);
            throw new RuntimeException("初始化月份工作日历失败: " + e.getMessage());
        }
    }
}
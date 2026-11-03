package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.WorkCalendar;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WorkCalendarMapper extends BaseMapper<WorkCalendar> {

    /**
     * 根据日期范围查询工作日历 - 使用注解
     */
    @Select("SELECT * FROM mes_work_calendar WHERE is_deleted = 0 AND calendar_date BETWEEN #{startDate} AND #{endDate} ORDER BY calendar_date ASC")
    List<WorkCalendar> selectByDateRange(@Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    /**
     * 根据日期查询工作日历 - 使用注解
     */
    @Select("SELECT * FROM mes_work_calendar WHERE is_deleted = 0 AND calendar_date = #{calendarDate}")
    WorkCalendar selectByDate(@Param("calendarDate") LocalDate calendarDate);

    /**
     * 根据日期类型查询 - 使用注解
     */
    @Select("<script>" +
            "SELECT * FROM mes_work_calendar WHERE is_deleted = 0 AND day_type = #{dayType} " +
            "<if test='startDate != null'>" +
            "AND calendar_date >= #{startDate} " +
            "</if>" +
            "<if test='endDate != null'>" +
            "AND calendar_date &lt;= #{endDate} " +
            "</if>" +
            "ORDER BY calendar_date ASC" +
            "</script>")
    List<WorkCalendar> selectByDayType(@Param("dayType") String dayType,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);

    /**
     * 检查日期是否存在 - 使用注解
     */
    @Select("SELECT COUNT(*) FROM mes_work_calendar WHERE is_deleted = 0 AND calendar_date = #{calendarDate}")
    int existsByDate(@Param("calendarDate") LocalDate calendarDate);

    /**
     * 根据班次ID查询工作日历 - 使用注解
     */
    @Select("SELECT * FROM mes_work_calendar WHERE is_deleted = 0 AND work_shift_id = #{workShiftId} ORDER BY calendar_date ASC")
    List<WorkCalendar> selectByWorkShiftId(@Param("workShiftId") Long workShiftId);

    /**
     * 获取指定日期范围内的有效工作日 - 使用注解
     */
    @Select("SELECT calendar_date FROM mes_work_calendar WHERE is_deleted = 0 AND day_type = 'WORKDAY' AND calendar_date BETWEEN #{startDate} AND #{endDate} ORDER BY calendar_date ASC")
    List<LocalDate> getWorkingDays(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);

    /**
     * 获取指定日期范围内的非工作日 - 使用注解
     */
    @Select("SELECT calendar_date FROM mes_work_calendar WHERE is_deleted = 0 AND day_type != 'WORKDAY' AND calendar_date BETWEEN #{startDate} AND #{endDate} ORDER BY calendar_date ASC")
    List<LocalDate> getNonWorkingDays(@Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate);

    @Insert("<script>" +
            "INSERT INTO mes_work_calendar (" +
            "calendar_date, day_type, work_shift_id, capacity_factor, remark, create_time, update_time" +
            ") VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(" +
            "#{item.calendarDate}, #{item.dayType}, #{item.workShiftId}, #{item.capacityFactor}, " +
            "#{item.remark}, #{item.createTime}, #{item.updateTime}" +
            ")" +
            "</foreach>" +
            " ON DUPLICATE KEY UPDATE " +
            "day_type = VALUES(day_type), " +
            "work_shift_id = VALUES(work_shift_id), " +
            "capacity_factor = VALUES(capacity_factor), " +
            "remark = VALUES(remark), " +
            "update_time = VALUES(update_time)" +
            "</script>")
    int batchInsertOrUpdate(@Param("list") List<WorkCalendar> calendars);
}
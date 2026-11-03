package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.mes.dto.StatusLogQueryDTO;
import com.mes.entity.EquipmentStatusLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface EquipmentStatusLogMapper extends BaseMapper<EquipmentStatusLog> {

    /**
     * 根据设备ID查询状态日志
     */
    List<EquipmentStatusLog> selectByEquipmentId(@Param("equipmentId") Long equipmentId,
                                                 @Param("limit") Integer limit);

    /**
     * 根据日志类型查询
     */
    List<EquipmentStatusLog> selectByLogType(@Param("logType") Integer logType);

    /**
     * 查询设备的最新状态
     */
    EquipmentStatusLog selectLatestStatus(@Param("equipmentId") Long equipmentId);

    /**
     * 查询设备的历史状态变化
     */
    List<EquipmentStatusLog> selectStatusHistory(@Param("equipmentId") Long equipmentId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 查询设备的报警记录
     */
    List<EquipmentStatusLog> selectAlarmLogs(@Param("equipmentId") Long equipmentId,
                                             @Param("limit") Integer limit);

    /**
     * 查询设备的点检记录
     */
    List<EquipmentStatusLog> selectInspectionLogs(@Param("equipmentId") Long equipmentId);

    /**
     * 条件分页查询状态日志
     */
    Page<EquipmentStatusLog> selectStatusLogPage(Page<EquipmentStatusLog> page,
                                                 @Param("query") StatusLogQueryDTO query);

    /**
     * 统计设备运行时长
     */
    Map<String, Object> sumRunningHours(@Param("equipmentId") Long equipmentId,
                                        @Param("startTime") LocalDateTime startTime,
                                        @Param("endTime") LocalDateTime endTime);

    /**
     * 统计设备停机时长
     */
    Map<String, Object> sumDowntimeHours(@Param("equipmentId") Long equipmentId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime);

    /**
     * 统计设备产量
     */
    Map<String, Object> sumOutput(@Param("equipmentId") Long equipmentId,
                                  @Param("startTime") LocalDateTime startTime,
                                  @Param("endTime") LocalDateTime endTime);

    /**
     * 统计设备能耗
     */
    Map<String, Object> sumEnergyConsumption(@Param("equipmentId") Long equipmentId,
                                             @Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    /**
     * 统计设备运行效率(OEE)
     */
    Map<String, Object> calculateOEE(@Param("equipmentId") Long equipmentId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime);

    /**
     * 批量插入状态日志
     */
    int insertBatchStatusLog(@Param("list") List<EquipmentStatusLog> list);

    /**
     * 清理过期的日志数据
     */
    int cleanExpiredLogs(@Param("days") Integer days,
                         @Param("logType") Integer logType);

    /**
     * 查询设备运行统计（按小时）
     */
    List<Map<String, Object>> selectHourlyStatistics(@Param("equipmentId") Long equipmentId,
                                                     @Param("date") String date);

    /**
     * 获取设备报警统计
     */
    List<Map<String, Object>> selectAlarmStatistics(@Param("equipmentId") Long equipmentId,
                                                    @Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备状态分布
     */
    List<Map<String, Object>> selectStatusDistribution(@Param("equipmentId") Long equipmentId,
                                                       @Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime);

    /**
     * 获取设备运行趋势（按天）
     */
    List<Map<String, Object>> selectDailyTrend(@Param("equipmentId") Long equipmentId,
                                               @Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
}
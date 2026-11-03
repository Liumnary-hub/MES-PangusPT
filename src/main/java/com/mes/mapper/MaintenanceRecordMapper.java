package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.mes.dto.MaintenanceQueryDTO;
import com.mes.entity.MaintenanceRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface MaintenanceRecordMapper extends BaseMapper<MaintenanceRecord> {

    /**
     * 根据设备ID查询维护记录
     */
    List<MaintenanceRecord> selectByEquipmentId(@Param("equipmentId") Long equipmentId);

    /**
     * 根据维护单号查询
     */
    MaintenanceRecord selectByMaintenanceNo(@Param("maintenanceNo") String maintenanceNo);

    /**
     * 根据维护类型查询
     */
    List<MaintenanceRecord> selectByRecordType(@Param("recordType") Integer recordType);

    /**
     * 根据维护状态查询
     */
    List<MaintenanceRecord> selectByStatus(@Param("status") Integer status);

    /**
     * 查询超期未完成的维护记录
     */
    List<MaintenanceRecord> selectOverdueRecords();

    /**
     * 查询指定日期的维护记录
     */
    List<MaintenanceRecord> selectByDate(@Param("date") LocalDateTime date);

    /**
     * 条件分页查询维护记录
     */
    Page<MaintenanceRecord> selectMaintenancePage(Page<MaintenanceRecord> page,
                                                  @Param("query") MaintenanceQueryDTO query);

    /**
     * 统计各类型维护数量
     */
    List<Map<String, Object>> countByRecordType();

    /**
     * 统计各状态维护数量
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 统计设备维护次数
     */
    List<Map<String, Object>> countMaintenanceByEquipment();

    /**
     * 统计维护成本（按月）
     */
    List<Map<String, Object>> sumCostByMonth(@Param("year") Integer year);

    /**
     * 更新维护状态
     */
    int updateMaintenanceStatus(@Param("recordId") Long recordId,
                                @Param("status") Integer status,
                                @Param("updateTime") LocalDateTime updateTime);

    /**
     * 完成维护记录
     */
    int completeMaintenance(@Param("recordId") Long recordId,
                            @Param("actualEndTime") LocalDateTime actualEndTime,
                            @Param("result") Integer result,
                            @Param("acceptor") String acceptor,
                            @Param("acceptanceTime") LocalDateTime acceptanceTime);

    /**
     * 查询设备的最近维护记录
     */
    MaintenanceRecord selectRecentByEquipmentId(@Param("equipmentId") Long equipmentId);

    /**
     * 查询维护记录详情（包含设备信息）
     */
    MaintenanceRecord selectDetailById(@Param("recordId") Long recordId);
}
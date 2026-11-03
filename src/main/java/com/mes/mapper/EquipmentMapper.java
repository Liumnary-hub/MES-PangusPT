package com.mes.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.dto.EquipmentQueryDTO;
import com.mes.entity.Equipment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {

    /**
     * 根据设备编码查询设备
     */
    Equipment selectByEquipmentCode(@Param("equipmentCode") String equipmentCode);

    /**
     * 根据车间查询设备列表
     */
    List<Equipment> selectByWorkshop(@Param("workshop") String workshop);

    /**
     * 根据设备状态查询设备列表
     */
    List<Equipment> selectByStatus(@Param("status") Integer status);

    /**
     * 查询需要保养的设备
     */
    List<Equipment> selectNeedMaintenanceEquipments();

    /**
     * 查询关键设备列表
     */
    List<Equipment> selectCriticalEquipments();

    /**
     * 条件分页查询设备
     */
    Page<Equipment> selectEquipmentPage(Page<Equipment> page, @Param("query") EquipmentQueryDTO query);

    /**
     * 根据设备类型统计数量
     */
    List<Map<String, Object>> countByEquipmentType();

    /**
     * 根据状态统计数量
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 根据车间统计设备数量
     */
    List<Map<String, Object>> countByWorkshop();

    /**
     * 更新设备状态
     */
    int updateEquipmentStatus(@Param("equipmentId") Long equipmentId,
                              @Param("status") Integer status,
                              @Param("updateTime") LocalDateTime updateTime,
                              @Param("updateBy") String updateBy);

    /**
     * 更新设备保养信息
     */
    int updateMaintenanceInfo(@Param("equipmentId") Long equipmentId,
                              @Param("lastMaintenanceDate") LocalDateTime lastMaintenanceDate,
                              @Param("nextMaintenanceDate") LocalDateTime nextMaintenanceDate,
                              @Param("totalRunningHours") Double totalRunningHours);

    /**
     * 更新设备二维码
     */
    int updateQrCode(@Param("equipmentId") Long equipmentId,
                     @Param("qrCode") String qrCode);

    /**
     * 查询设备及其最近维护记录
     */
    Equipment selectWithRecentMaintenance(@Param("equipmentId") Long equipmentId);

    /**
     * 批量更新设备状态
     */
    int batchUpdateStatus(@Param("equipmentIds") List<Long> equipmentIds,
                          @Param("status") Integer status,
                          @Param("updateBy") String updateBy);
}
package com.mes.dto;

import lombok.Data;

import java.util.List;

/**
 * 设备统计DTO
 */
@Data
public class EquipmentStatisticsDTO {

    // 设备数量统计
    private Long totalCount;
    private Long runningCount;
    private Long maintenanceCount;
    private Long standbyCount;
    private Long stoppedCount;

    // 设备类型分布
    private List<TypeDistribution> typeDistributions;

    // 车间分布
    private List<EquipmentStatisticsDTO.WorkshopDistribution> workshopDistributions;

    // 保养情况
    private Integer needMaintenanceCount;
    private Integer maintenanceOverdueCount;

    // 关键设备
    private Long criticalEquipmentCount;

    @Data
    public static class TypeDistribution {
        private String equipmentType;
        private Long count;
        private Double percentage;
    }

    @Data
    public static class WorkshopDistribution {
        private String workshop;
        private Long count;
        private Long criticalCount;
        private Double percentage;
    }
}

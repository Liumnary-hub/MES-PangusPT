package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备运行状态日志实体类 - 用于监控、统计和点检
 */
@Data
@Entity
@Table(name = "mes_equipment_status_log")

 public class EquipmentStatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    /**
     * 关联的设备
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    /**
     * 日志类型：1-状态变更, 2-运行参数, 3-点检记录, 4-报警记录, 5-产量记录
     */
    @Column(name = "log_type", nullable = false)
    private Integer logType;

    /**
     * 记录时间
     */
    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    /**
     * 记录人/数据来源
     */
    @Column(name = "recorder", length = 50)
    private String recorder;

    /**
     * 设备状态：1-运行, 2-待机, 3-维修, 4-保养, 5-停机
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 运行参数（JSON格式，如：{"speed":100,"temperature":60,"pressure":80}）
     */
    @Column(name = "running_params", columnDefinition = "TEXT")
    private String runningParams;

    /**
     * 报警信息
     */
    @Column(name = "alarm_info", length = 500)
    private String alarmInfo;

    /**
     * 报警代码
     */
    @Column(name = "alarm_code", length = 50)
    private String alarmCode;

    /**
     * 报警级别：1-提示, 2-警告, 3-严重, 4-紧急
     */
    @Column(name = "alarm_level")
    private Integer alarmLevel;

    /**
     * 点检结果（JSON格式）
     */
    @Column(name = "inspection_result", columnDefinition = "TEXT")
    private String inspectionResult;

    /**
     * 点检是否合格：1-合格, 0-不合格
     */
    @Column(name = "inspection_qualified")
    private Boolean inspectionQualified;

    /**
     * 点检人
     */
    @Column(name = "inspector", length = 50)
    private String inspector;

    /**
     * 产量计数
     */
    @Column(name = "output_count")
    private Integer outputCount;

    /**
     * 良品数
     */
    @Column(name = "good_count")
    private Integer goodCount;

    /**
     * 不良品数
     */
    @Column(name = "defect_count")
    private Integer defectCount;

    /**
     * 运行时长（小时）
     */
    @Column(name = "running_hours")
    private Double runningHours;

    /**
     * 能耗（千瓦时）
     */
    @Column(name = "energy_consumption")
    private Double energyConsumption;

    /**
     * 操作员
     */
    @Column(name = "operator", length = 50)
    private String operator;

    /**
     * 班次
     */
    @Column(name = "shift", length = 20)
    private String shift;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @Transient
    public Double getYieldRate() {
        if (outputCount != null && outputCount > 0 && goodCount != null) {
            return (goodCount * 100.0) / outputCount;
        }
        return 0.0;
    }

    @Transient
    public Double getDefectRate() {
        if (outputCount != null && outputCount > 0 && defectCount != null) {
            return (defectCount * 100.0) / outputCount;
        }
        return 0.0;
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (recordTime == null) {
            recordTime = LocalDateTime.now();
        }
    }
}
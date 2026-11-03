package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备故障记录实体类
 */
@Data
@Entity
@Table(name = "mes_fault_record")
public class FaultRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fault_id")
    private Long faultId;

    /**
     * 故障单号
     */
    @Column(name = "fault_no", unique = true, length = 50)
    private String faultNo;

    /**
     * 关联的设备
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    /**
     * 故障类型：1-机械故障, 2-电气故障, 3-控制系统故障, 4-软件故障, 5-其他
     */
    @Column(name = "fault_type")
    private Integer faultType;

    /**
     * 故障等级：1-轻微(不影响生产), 2-一般(影响单机), 3-严重(影响产线), 4-重大(影响全厂)
     */
    @Column(name = "fault_level")
    private Integer faultLevel = 2;

    /**
     * 故障状态：1-已上报, 2-处理中, 3-已解决, 4-已关闭
     */
    @Column(name = "fault_status")
    private Integer faultStatus = 1;

    /**
     * 故障发生时间
     */
    @Column(name = "occur_time", nullable = false)
    private LocalDateTime occurTime;

    /**
     * 故障发现人
     */
    @Column(name = "discoverer", length = 50)
    private String discoverer;

    /**
     * 发现人工号
     */
    @Column(name = "discoverer_id", length = 50)
    private String discovererId;

    /**
     * 故障现象描述
     */
    @Column(name = "phenomenon", columnDefinition = "TEXT", nullable = false)
    private String phenomenon;

    /**
     * 故障原因分析
     */
    @Column(name = "cause_analysis", columnDefinition = "TEXT")
    private String causeAnalysis;

    /**
     * 临时措施
     */
    @Column(name = "temporary_measures", columnDefinition = "TEXT")
    private String temporaryMeasures;

    /**
     * 处理措施
     */
    @Column(name = "treatment_measures", columnDefinition = "TEXT")
    private String treatmentMeasures;

    /**
     * 处理开始时间
     */
    @Column(name = "treatment_start_time")
    private LocalDateTime treatmentStartTime;

    /**
     * 处理完成时间
     */
    @Column(name = "treatment_end_time")
    private LocalDateTime treatmentEndTime;

    /**
     * 处理人
     */
    @Column(name = "handler", length = 50)
    private String handler;

    /**
     * 处理人工号
     */
    @Column(name = "handler_id", length = 50)
    private String handlerId;

    /**
     * 实际停机时长（小时）
     */
    @Column(name = "actual_downtime_hours")
    private Double actualDowntimeHours;

    /**
     * 影响范围描述
     */
    @Column(name = "impact_scope", columnDefinition = "TEXT")
    private String impactScope;

    /**
     * 影响产量（件）
     */
    @Column(name = "impact_output")
    private Integer impactOutput;

    /**
     * 经济损失估算（元）
     */
    @Column(name = "estimated_loss")
    private Double estimatedLoss;

    /**
     * 根本原因
     */
    @Column(name = "root_cause", columnDefinition = "TEXT")
    private String rootCause;

    /**
     * 预防措施
     */
    @Column(name = "preventive_measures", columnDefinition = "TEXT")
    private String preventiveMeasures;

    /**
     * 是否重复故障
     */
    @Column(name = "is_recurring")
    private Boolean isRecurring = false;

    /**
     * 关联的维护记录ID
     */
    @Column(name = "related_maintenance_id")
    private Long relatedMaintenanceId;

    /**
     * 故障图片（多个图片用逗号分隔）
     */
    @Column(name = "fault_images", length = 1000)
    private String faultImages;

    /**
     * 关闭人
     */
    @Column(name = "closer", length = 50)
    private String closer;

    /**
     * 关闭时间
     */
    @Column(name = "close_time")
    private LocalDateTime closeTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_by", length = 50)
    private String createBy;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Column(name = "update_by", length = 50)
    private String updateBy;

    @Transient
    public Double getTreatmentDuration() {
        if (treatmentStartTime != null && treatmentEndTime != null) {
            return (double) java.time.Duration.between(treatmentStartTime, treatmentEndTime).toHours();
        }
        return 0.0;
    }

    @Transient
    public boolean isOverdue() {
        if (faultStatus == 2) { // 处理中
            LocalDateTime deadline = occurTime.plusHours(24); // 24小时内处理
            return LocalDateTime.now().isAfter(deadline);
        }
        return false;
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (faultNo == null) {
            faultNo = "FT" + System.currentTimeMillis();
        }
        if (occurTime == null) {
            occurTime = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();

        // 自动计算停机时长
        if (treatmentStartTime != null && treatmentEndTime != null) {
            actualDowntimeHours = getTreatmentDuration();
        }

        // 故障关闭时记录关闭信息
        if (faultStatus == 4 && closer == null) {
            closer = createBy;
            closeTime = LocalDateTime.now();
        }
    }
}
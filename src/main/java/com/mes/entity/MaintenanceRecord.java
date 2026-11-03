package com.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备维护保养记录实体类
 */
@Data
@Entity
@Table(name = "mes_maintenance_record")
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Long maintenanceId;

    /**
     * 维护单号
     */
    @Column(name = "maintenance_no", unique = true, length = 50)
    private String maintenanceNo;

    /**
     * 关联的设备 - 这个属性是必须的！
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;  // 属性名必须为 equipment，与 mappedBy 的值匹配

    /**
     * 维护类型：1-日常保养, 2-定期保养, 3-预防性维护, 4-预见性维护, 5-计划外维修
     */
    @Column(name = "maintenance_type")
    private Integer maintenanceType;

    /**
     * 维护类别：1-一级保养, 2-二级保养, 3-大修
     */
    @Column(name = "maintenance_category")
    private Integer maintenanceCategory;

    /**
     * 维护状态：1-计划中, 2-进行中, 3-已完成, 4-已取消
     */
    @Column(name = "maintenance_status")
    private Integer maintenanceStatus = 1;

    /**
     * 计划开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_start_time")
    private LocalDateTime planStartTime;

    /**
     * 计划完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "plan_end_time")
    private LocalDateTime planEndTime;

    /**
     * 实际开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_start_time")
    private LocalDateTime actualStartTime;

    /**
     * 实际完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "actual_end_time")
    private LocalDateTime actualEndTime;

    /**
     * 维护负责人
     */
    @Column(name = "responsible_person", length = 50)
    private String responsiblePerson;

    /**
     * 负责人工号
     */
    @Column(name = "responsible_person_id", length = 50)
    private String responsiblePersonId;

    /**
     * 维护班组
     */
    @Column(name = "maintenance_team", length = 50)
    private String maintenanceTeam;

    /**
     * 维护内容描述
     */
    @Column(name = "maintenance_content", columnDefinition = "TEXT", nullable = false)
    private String maintenanceContent;

    /**
     * 维护标准/要求
     */
    @Column(name = "maintenance_standard", columnDefinition = "TEXT")
    private String maintenanceStandard;

    /**
     * 维护结果
     */
    @Column(name = "maintenance_result", columnDefinition = "TEXT")
    private String maintenanceResult;

    /**
     * 是否合格：1-合格, 0-不合格
     */
    @Column(name = "is_qualified")
    private Boolean isQualified = true;

    /**
     * 更换备件清单（JSON格式）
     */
    @Column(name = "replacement_parts", columnDefinition = "TEXT")
    private String replacementParts;

    /**
     * 备件成本（元）
     */
    @Column(name = "parts_cost")
    private Double partsCost;

    /**
     * 人工成本（元）
     */
    @Column(name = "labor_cost")
    private Double laborCost;

    /**
     * 总成本（元）
     */
    @Column(name = "total_cost")
    private Double totalCost;

    /**
     * 维护时长（小时）
     */
    @Column(name = "maintenance_duration")
    private Double maintenanceDuration;

    /**
     * 影响停机时长（小时）
     */
    @Column(name = "downtime_hours")
    private Double downtimeHours;

    /**
     * 维护前设备状态
     */
    @Column(name = "before_maintenance_status")
    private Integer beforeMaintenanceStatus;

    /**
     * 维护后设备状态
     */
    @Column(name = "after_maintenance_status")
    private Integer afterMaintenanceStatus;

    /**
     * 维护图片（多个图片用逗号分隔）
     */
    @Column(name = "maintenance_images", length = 1000)
    private String maintenanceImages;

    /**
     * 验收人
     */
    @Column(name = "acceptor", length = 50)
    private String acceptor;

    /**
     * 验收时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "acceptance_time")
    private LocalDateTime acceptanceTime;

    /**
     * 验收意见
     */
    @Column(name = "acceptance_opinion", length = 500)
    private String acceptanceOpinion;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @Column(name = "update_by", length = 50)
    private String updateBy;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    @Transient
    public Double getActualDuration() {
        if (actualStartTime != null && actualEndTime != null) {
            return (double) java.time.Duration.between(actualStartTime, actualEndTime).toHours();
        }
        return 0.0;
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (maintenanceNo == null) {
            maintenanceNo = "MT" + System.currentTimeMillis();
        }
        if (planStartTime == null) {
            planStartTime = LocalDateTime.now();
        }
        // 如果计划结束时间为空，默认计划2小时后完成
        if (planEndTime == null) {
            planEndTime = planStartTime.plusHours(2);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();

        // 维护完成时自动计算时长
        if (maintenanceStatus == 3 && actualStartTime != null && actualEndTime != null) {
            maintenanceDuration = getActualDuration();
        }

        // 自动计算总成本
        if (partsCost != null || laborCost != null) {
            totalCost = (partsCost != null ? partsCost : 0) + (laborCost != null ? laborCost : 0);
        }
    }
}
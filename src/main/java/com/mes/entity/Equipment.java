package com.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 设备资产实体类 - MES设备管理核心
 */
@Data
@Entity
@Table(name = "mes_equipment")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_id")
    private Long equipmentId;

    /**
     * 设备唯一编码
     */
    @Column(name = "equipment_code", unique = true, nullable = false, length = 50)
    private String equipmentCode;

    /**
     * 设备名称
     */
    @Column(name = "equipment_name", nullable = false, length = 100)
    private String equipmentName;

    /**
     * 设备类型：CNC/注塑机/组装线等
     */
    @Column(name = "equipment_type", length = 50)
    private String equipmentType;

    /**
     * 设备状态：1-运行中, 2-待机中, 3-维修中, 4-保养中, 5-已停机, 6-已报废
     */
    @Column(name = "status")
    private Integer status = 1;

    /**
     * 所属车间
     */
    @Column(name = "workshop", length = 100)
    private String workshop;

    /**
     * 所在工位/位置
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * 设备型号
     */
    @Column(name = "model", length = 50)
    private String model;

    /**
     * 设备规格
     */
    @Column(name = "specification", length = 100)
    private String specification;

    /**
     * 供应商
     */
    @Column(name = "supplier", length = 200)
    private String supplier;

    /**
     * 制造商
     */
    @Column(name = "manufacturer", length = 200)
    private String manufacturer;

    /**
     * 购置日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    /**
     * 投入使用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "commissioning_date")
    private LocalDateTime commissioningDate;

    /**
     * 设备负责人
     */
    @Column(name = "responsible_person", length = 50)
    private String responsiblePerson;

    /**
     * 负责人工号
     */
    @Column(name = "responsible_person_id", length = 50)
    private String responsiblePersonId;

    /**
     * 联系电话
     */
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    /**
     * 是否关键设备
     */
    @Column(name = "is_critical")
    private Boolean isCritical = false;

    /**
     * 保养周期（天）
     */
    @Column(name = "maintenance_cycle")
    private Integer maintenanceCycle = 30;

    /**
     * 上次保养日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_maintenance_date")
    private LocalDateTime lastMaintenanceDate;

    /**
     * 下次保养日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "next_maintenance_date")
    private LocalDateTime nextMaintenanceDate;

    /**
     * 累计运行时长（小时）
     */
    @Column(name = "total_running_hours")
    private Double totalRunningHours = 0.0;

    /**
     * 设备原值
     */
    @Column(name = "original_value", precision = 12, scale = 2)
    private BigDecimal originalValue;

    /**
     * 设备净值
     */
    @Column(name = "net_value", precision = 12, scale = 2)
    private BigDecimal netValue;

    /**
     * 设备二维码
     */
    @Column(name = "qr_code", length = 100)
    private String qrCode;

    /**
     * 技术参数（JSON格式）
     */
    @Column(name = "technical_params", columnDefinition = "TEXT")
    private String technicalParams;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 是否启用
     */
    @Column(name = "is_active")
    private Boolean isActive = true;

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
     * 关联的维护记录
     */
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MaintenanceRecord> maintenanceRecords = new ArrayList<>();

    /**
     * 关联的故障记录
     */
    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FaultRecord> faultRecords = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        // 自动生成二维码
        if (qrCode == null && equipmentCode != null) {
            qrCode = "EQ_" + equipmentCode;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
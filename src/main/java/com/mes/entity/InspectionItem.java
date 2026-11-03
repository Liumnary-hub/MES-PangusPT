package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// InspectionItem.java - 检验项目
@Entity
@Table(name = "mes_inspection_item")
@Data
public class InspectionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long standardId;              // 检验标准ID

    @Column(nullable = false, length = 50)
    private String itemCode;              // 项目编码

    @Column(nullable = false, length = 100)
    private String itemName;              // 项目名称

    @Column(length = 50)
    private String inspectionMethod;      // 检验方法: 目视, 测量, 试验, 仪器

    @Column(length = 20)
    private String unit;                  // 单位

    @Column(length = 50)
    private String standardValue;         // 标准值

    private Double upperLimit;            // 上限值
    private Double lowerLimit;            // 下限值

    @Column(length = 20)
    private String dataType = "numeric";  // 数据类型: numeric(数值), text(文本), option(选项)

    @Column(columnDefinition = "text")
    private String options;               // 选项值(JSON格式，当dataType=option时使用)

    @Column(nullable = false)
    private Boolean isCritical = false;   // 是否关键项

    @Column(nullable = false)
    private Integer sequence = 1;         // 排序

    @Column(length = 100)
    private String equipment;             // 使用设备

    @Column(length = 500)
    private String remark;                // 备注

    @Column(length = 20)
    private String status = "ACTIVE";     // 状态: ACTIVE(激活), INACTIVE(未激活), DELETED(删除)

    @Column(nullable = false)
    private Integer deleted = 0;          // 逻辑删除标记: 0-正常, 1-已删除

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime;     // 更新时间

    // 构造函数
    public InspectionItem() {
        this.createTime = LocalDateTime.now();
        this.status = "ACTIVE";
        this.deleted = 0;
    }

    // 逻辑删除方法
    public void markAsDeleted() {
        this.deleted = 1;
        this.updateTime = LocalDateTime.now();
        this.status = "DELETED";
    }

    // 更新状态方法
    public void updateStatus(String status) {
        this.status = status;
        this.updateTime = LocalDateTime.now();
    }
}
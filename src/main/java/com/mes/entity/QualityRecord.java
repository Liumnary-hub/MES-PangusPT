package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// QualityRecord.java - 检验记录
@Entity
@Table(name = "mes_quality_record")
@Data
public class QualityRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long taskId;                  // 任务ID

    @Column(nullable = false)
    private Long planId;                  // 计划ID

    @Column(nullable = false)
    private Long itemId;                  // 项目ID

    @Column(nullable = false, length = 50)
    private String itemCode;              // 项目编码

    @Column(nullable = false, length = 100)
    private String itemName;              // 项目名称

    @Column(length = 50)
    private String inspectionMethod;      // 检验方法

    @Column(length = 20)
    private String unit;                  // 单位

    private Double standardValue;         // 标准值
    private Double upperLimit;            // 上限
    private Double lowerLimit;            // 下限
    private Double actualValue;           // 实际值

    @Column(length = 20)
    private String result;                // 结果: qualified(合格), defective(不合格)

    @Column(length = 50)
    private String defectCode;            // 缺陷代码

    @Column(length = 500)
    private String defectDescription;     // 缺陷描述

    @Column(length = 500)
    private String remark;                // 备注

    @Column(length = 1000)
    private String images;                // 图片路径(JSON数组)

    @Column(length = 50)
    private String operator;              // 操作员

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    // 添加逻辑删除字段
    @Column(nullable = false, columnDefinition = "int default 0")
    private Integer deleted = 0;          // 逻辑删除标记: 0-未删除, 1-已删除

    // 可选：添加其他可能需要的字段

    @Column(length = 50)
    private String inspector;             // 检验员

    @Column
    private Integer sequence;             // 序号

    @Column(length = 50)
    private String status;                // 状态: pending(待检), inspected(已检)

    @Column
    private LocalDateTime inspectTime;    // 检验时间

    @Column
    private Double deviation;             // 偏差值

    // 可选：添加构造函数

    public QualityRecord() {
        // 默认构造函数
    }

    public QualityRecord(Long taskId, String itemCode, String itemName, Double standardValue, Double actualValue) {
        this.taskId = taskId;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.standardValue = standardValue;
        this.actualValue = actualValue;
        this.deleted = 0;
    }
}
package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// QualityPlanItem.java - 检验计划明细
@Entity
@Table(name = "mes_quality_plan_item")
@Data
public class QualityPlanItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long planId;                  // 计划ID

    @Column(nullable = false)
    private Long standardId;              // 检验标准ID

    @Column(nullable = false)
    private Long itemId;                  // 检验项目ID

    @Column(nullable = false, length = 100)
    private String itemName;              // 项目名称

    private Integer sequence;             // 顺序

    @Column(nullable = false)
    private Boolean required = true;      // 是否必检

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    @Column(nullable = false)
    private Integer deleted = 0;          // 逻辑删除标记

    // 其他可能需要的字段
    private LocalDateTime updateTime;     // 更新时间

    @Transient
    private String standardCode;          // 标准编码（瞬态）
    @Transient
    private String standardName;          // 标准名称（瞬态）
}
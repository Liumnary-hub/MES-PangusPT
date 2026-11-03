package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

// QualityStatistic.java - 质量统计
@Entity
@Table(name = "mes_quality_statistic")
@Data
public class QualityStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String statisticDate;         // 统计日期(YYYY-MM-DD)

    @Column(length = 50)
    private String productCode;           // 产品编码

    @Column(length = 100)
    private String productName;           // 产品名称

    @Column(length = 50)
    private String workshopCode;          // 车间编码

    @Column(length = 20)
    private String inspectionType;        // 检验类型

    private Integer totalTasks = 0;       // 总任务数
    private Integer completedTasks = 0;   // 已完成任务数

    private Integer totalChecked = 0;     // 总检验数量
    private Integer totalQualified = 0;   // 总合格数量
    private Integer totalDefective = 0;   // 总缺陷数量

    private Double passRate = 0.0;        // 合格率

    private Double defectRate = 0.0;      // 缺陷率
    private Double dpmo = 0.0;            // DPMO(百万机会缺陷数)

    @Column(columnDefinition = "text")
    private String defectDistribution;    // 缺陷分布(JSON)

    @Column(columnDefinition = "text")
    private String spcData;               // SPC数据(JSON)

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private LocalDateTime updateTime;     // 更新时间
}
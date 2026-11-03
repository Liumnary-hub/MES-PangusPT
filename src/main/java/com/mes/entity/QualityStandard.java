package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// QualityStandard.java - 检验标准主表
@Entity
@Table(name = "mes_quality_standard")
@Data
public class QualityStandard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String standardCode;          // 标准编号

    @Column(nullable = false, length = 100)
    private String standardName;          // 标准名称

    @Column(nullable = false)
    private Long productId;               // 产品ID

    @Column(length = 20)
    private String inspectionType;        // 检验类型: IQC(来料), IPQC(过程), FQC(成品), OQC(出货)

    @Column(length = 20)
    private String inspectionLevel;       // 检验水平: I, II, III

    @Column(length = 20)
    private String aqlLevel;              // AQL水平: 0.65, 1.0, 1.5, 2.5, 4.0

    @Column(columnDefinition = "text")
    private String acceptanceCriteria;    // 接收标准

    @Column(length = 20)
    private String status = "draft";      // 状态: draft(草稿), active(生效), inactive(失效)

    @Column(length = 50)
    private String version = "1.0";       // 版本号

    @Column(length = 500)
    private String remark;                // 备注

    private LocalDateTime effectiveDate;  // 生效日期
    private LocalDateTime expireDate;     // 失效日期

    @Column(nullable = false)
    private String createBy;              // 创建人（改为String类型）

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private String updateBy;              // 更新人（改为String类型）
    private LocalDateTime updateTime;     // 更新时间

    @Column(nullable = false)
    private Integer deleted = 0;          // 逻辑删除标记: 0-正常, 1-已删除

    @Transient
    private String productCode;           // 产品编码（非持久化）
    @Transient
    private String productName;           // 产品名称（非持久化）
    @Transient
    private List<InspectionItem> items;   // 检验项目列表
}
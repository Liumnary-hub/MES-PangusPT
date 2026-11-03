package com.mes.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

// QualityPlan.java - 质检计划
// QualityPlan.java - 检验计划
@Entity
@Table(name = "mes_quality_plan")
@Data
public class QualityPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String planNo;                // 计划编号

    @Column(nullable = false, length = 100)
    private String planName;              // 计划名称

    @Column(nullable = false)
    private Long productId;               // 产品ID

    @Column(name = "standard_id", nullable = false)
    private Long standardId;              // 标准ID

    @Column(length = 50)
    private String batchNo;               // 批次号

    @Column(length = 20)
    private String planType;              // 计划类型: first(首件), patrol(巡检), final(末件), spot(抽检)

    @Column(length = 20)
    private String frequency;             // 频率: per_batch(每批), daily(每天), weekly(每周), monthly(每月)

    @Column(nullable = false)
    private LocalDateTime planDate;       // 计划日期

    private Integer sampleSize;           // 抽样数量
    private Integer totalQuantity;        // 总数量

    @Column(length = 50)
    private String inspector;             // 检验员

    @Column(length = 20)
    private String status = "pending";    // 状态: pending(待执行), in_progress(执行中), completed(已完成), cancelled(已取消)

    @Column(length = 50)
    private String approvalBy;            // 审批人

    private LocalDateTime approvalTime;   // 审批时间

    @Column(length = 500)
    private String remark;                // 备注

    @Column(nullable = false)
    private Long createBy;                // 创建人

    @Column(nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    private Long updateBy;                // 更新人
    private LocalDateTime updateTime;     // 更新时间

    @Transient
    private String productCode;           // 产品编码
    @Transient
    private String productName;           // 产品名称
    @Transient
    private String standardCode;          // 标准编码
    @Transient
    private String standardName;          // 标准名称
    @Transient
    private List<QualityPlanItem> items;  // 计划明细

    // 其他字段
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;  // 调度时间

    @Column(name = "complete_time")
    private LocalDateTime completeTime;   // 完成时间

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;          // 逻辑删除标记
}
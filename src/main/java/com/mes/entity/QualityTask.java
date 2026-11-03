package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("mes_quality_task")
public class QualityTask {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("task_no")
    private String taskNo;                // 任务编号

    @TableField("plan_id")
    private Long planId;                  // 计划ID

    @TableField("standard_id")
    private Long standardId;              // 标准ID

    @TableField("product_code")
    private String productCode;           // 产品编码

    @TableField("product_name")
    private String productName;           // 产品名称

    @TableField("batch_no")
    private String batchNo;               // 批次号

    @TableField("order_no")
    private String orderNo;               // 生产订单号

    @TableField("workstation_code")
    private String workstationCode;       // 工位编码

    @TableField("inspector")
    private String inspector;             // 检验员

    @TableField("inspection_date")
    private LocalDateTime inspectionDate; // 检验日期

    @TableField("quantity_checked")
    private Integer quantityChecked;      // 检验数量

    @TableField("quantity_qualified")
    private Integer quantityQualified;    // 合格数量

    @TableField("quantity_defective")
    private Integer quantityDefective;    // 缺陷数量

    @TableField("result")
    private String result;                // 检验结果: qualified(合格), conditional(条件接收), rejected(拒收)

    @TableField("status")
    private String status = "PENDING";    // 状态: PENDING(待检), INSPECTING(检验中), COMPLETED(已完成)

    @TableField("inspection_type")
    private String inspectionType;        // 检验类型: INCOMING, PROCESS, FINAL

    @TableField("remark")
    private String remark;                // 备注

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime = LocalDateTime.now();

    @TableField("complete_time")
    private LocalDateTime completeTime;   // 完成时间

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;     // 更新时间

    @TableField("create_by")
    private String createBy;              // 创建人

    @TableField("update_by")
    private String updateBy;              // 更新人

    @TableField("urgent")
    private Boolean urgent;               // 是否紧急

    @TableField("workshop_code")
    private String workshopCode;          // 车间编码

    @TableField("line_code")
    private String lineCode;              // 产线编码

    // 逻辑删除字段（重要）
    @TableLogic(value = "0", delval = "1")
    @TableField("deleted")
    private Integer deleted = 0;          // 逻辑删除标记: 0-未删除, 1-已删除

    @TableField(exist = false)
    private List<QualityRecord> records;  // 检验记录

    @TableField(exist = false)
    private List<DefectDetail> defects;   // 缺陷明细

    // ============ 可选：添加其他可能需要的字段 ============

    @TableField("priority")
    private Integer priority;             // 优先级：1-低, 2-中, 3-高, 4-紧急

    @TableField("due_date")
    private LocalDateTime dueDate;        // 截止日期

    @TableField("sample_quantity")
    private Integer sampleQuantity;       // 抽样数量

    @TableField("inspection_standard")
    private String inspectionStandard;    // 检验标准

    @TableField("pass_rate")
    private Double passRate;              // 合格率

    @TableField("progress")
    private Integer progress;             // 进度百分比

    // ============ 如果需要，添加构造函数 ============

    public QualityTask() {
        // 默认构造函数
    }

    public QualityTask(String productCode, String productName, String inspector) {
        this.productCode = productCode;
        this.productName = productName;
        this.inspector = inspector;
        this.status = "PENDING";
        this.deleted = 0;
    }
}
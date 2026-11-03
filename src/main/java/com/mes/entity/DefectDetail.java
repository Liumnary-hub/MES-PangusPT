package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 缺陷明细表
 */
@Data
@TableName("mes_defect_detail")
public class DefectDetail {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "record_id")
    private Long recordId;                // 检验记录ID

    @TableField(value = "task_id")
    private Long taskId;                  // 检验任务ID（可选）

    @TableField(value = "defect_code_id")
    private Long defectCodeId;            // 缺陷代码ID

    @TableField(value = "defect_code")
    private String defectCode;            // 缺陷代码

    @TableField(value = "defect_name")
    private String defectName;            // 缺陷名称

    @TableField(value = "defect_quantity")
    private Integer defectQuantity = 1;   // 缺陷数量

    @TableField(value = "defect_location")
    private String defectLocation;        // 缺陷位置

    @TableField(value = "severity")
    private String severity;              // 严重程度: MINOR, MAJOR, CRITICAL, FATAL

    @TableField(value = "description")
    private String description;           // 缺陷描述

    @TableField(value = "cause_analysis")
    private String causeAnalysis;         // 原因分析

    @TableField(value = "temporary_action")
    private String temporaryAction;       // 临时措施

    @TableField(value = "permanent_action")
    private String permanentAction;       // 永久措施

    @TableField(value = "responsible_person")
    private String responsiblePerson;     // 责任人

    @TableField(value = "remark")
    private String remark;                // 备注

    @TableField(value = "images")
    private String images;                // 图片路径（JSON数组）

    @TableField(value = "create_by")
    private String createBy;              // 创建人

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;     // 创建时间

    @TableField(value = "update_by")
    private String updateBy;              // 更新人

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;     // 更新时间

    @TableField(value = "deleted")
    @TableLogic
    private Integer deleted = 0;          // 逻辑删除标志

    // 关联字段（非数据库字段）
    @TableField(exist = false)
    private String productCode;           // 产品代码

    @TableField(exist = false)
    private String productName;           // 产品名称

    @TableField("batch_no")
    private String batchNo;               // 批次号

    @TableField(exist = false)
    private String processCode;           // 工序代码

    @TableField(exist = false)
    private String processName;           // 工序名称

    /**
     * 判断是否为严重缺陷
     */
    public boolean isCritical() {
        return "CRITICAL".equals(severity) || "FATAL".equals(severity);
    }

    /**
     * 获取缺陷类型（通过缺陷代码关联获取）
     */
    @TableField(exist = false)
    private String defectType;

    /**
     * 获取责任部门（通过缺陷代码关联获取）
     */
    @TableField(exist = false)
    private String responsibleDept;
}
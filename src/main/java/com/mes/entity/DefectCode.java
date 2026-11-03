package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 缺陷代码表
 */
@Data
@TableName("mes_defect_code")
public class DefectCode {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "defect_code")
    private String defectCode;            // 缺陷代码

    @TableField(value = "defect_name")
    private String defectName;            // 缺陷名称

    @TableField(value = "defect_type")
    private String defectType;            // 缺陷类型: APPEARANCE, DIMENSION, FUNCTION, PERFORMANCE, SAFETY

    @TableField(value = "severity")
    private String severity;              // 严重程度: MINOR, MAJOR, CRITICAL, FATAL

    @TableField(value = "description")
    private String description;           // 缺陷描述

    @TableField(value = "handling_method")
    private String handlingMethod;        // 处理方法

    @TableField(value = "preventive_action")
    private String preventiveAction;      // 预防措施

    @TableField(value = "responsible_dept")
    private String responsibleDept;       // 责任部门

    @TableField(value = "enabled")
    private Boolean enabled = true;       // 是否启用

    @TableField(value = "remark")
    private String remark;                // 备注

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

    /**
     * 获取缺陷类型显示名称
     */
    @TableField(exist = false)
    private String defectTypeName;

    /**
     * 获取严重程度显示名称
     */
    @TableField(exist = false)
    private String severityName;

    /**
     * 判断是否为严重缺陷
     */
    public boolean isCritical() {
        return "CRITICAL".equals(severity) || "FATAL".equals(severity);
    }
}
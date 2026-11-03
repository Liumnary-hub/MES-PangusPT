package com.mes.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 缺陷代码VO（返回给前端的视图对象）
 */
@Data
@Schema(description = "缺陷代码视图对象")
public class DefectCodeVO {

    @Schema(description = "缺陷代码ID")
    private Long id;

    @Schema(description = "缺陷代码")
    private String defectCode;

    @Schema(description = "缺陷名称")
    private String defectName;

    @Schema(description = "缺陷类型: APPEARANCE-外观, DIMENSION-尺寸, FUNCTION-功能, PERFORMANCE-性能, SAFETY-安全")
    private String defectType;

    @Schema(description = "缺陷类型显示名称")
    private String defectTypeName;

    @Schema(description = "严重程度: MINOR-轻微, MAJOR-一般, CRITICAL-严重, FATAL-致命")
    private String severity;

    @Schema(description = "严重程度显示名称")
    private String severityName;

    @Schema(description = "缺陷描述")
    private String description;

    @Schema(description = "处理方法")
    private String handlingMethod;

    @Schema(description = "预防措施")
    private String preventiveAction;

    @Schema(description = "责任部门")
    private String responsibleDept;

    @Schema(description = "责任部门名称")
    private String responsibleDeptName;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建人姓名")
    private String createByName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "更新人姓名")
    private String updateByName;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "使用次数（统计）")
    private Long usageCount;

    @Schema(description = "最近使用时间")
    private LocalDateTime lastUsedTime;

    /**
     * 获取缺陷类型显示名称
     */
    public String getDefectTypeName() {
        if (defectType == null) {
            return "";
        }
        switch (defectType) {
            case "APPEARANCE":
                return "外观";
            case "DIMENSION":
                return "尺寸";
            case "FUNCTION":
                return "功能";
            case "PERFORMANCE":
                return "性能";
            case "SAFETY":
                return "安全";
            default:
                return defectType;
        }
    }

    /**
     * 获取严重程度显示名称
     */
    public String getSeverityName() {
        if (severity == null) {
            return "";
        }
        switch (severity) {
            case "MINOR":
                return "轻微";
            case "MAJOR":
                return "一般";
            case "CRITICAL":
                return "严重";
            case "FATAL":
                return "致命";
            default:
                return severity;
        }
    }

    /**
     * 判断是否为严重缺陷
     */
    @Schema(description = "是否为严重缺陷")
    public boolean isCritical() {
        return "CRITICAL".equals(severity) || "FATAL".equals(severity);
    }
}
package com.mes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 缺陷代码查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "缺陷代码查询参数")
public class DefectCodeQueryDTO extends BaseQueryDTO {

    @Schema(description = "缺陷代码")
    private String defectCode;

    @Schema(description = "缺陷名称")
    private String defectName;

    @Schema(description = "缺陷类型: APPEARANCE-外观, DIMENSION-尺寸, FUNCTION-功能, PERFORMANCE-性能, SAFETY-安全")
    private String defectType;

    @Schema(description = "严重程度: MINOR-轻微, MAJOR-一般, CRITICAL-严重, FATAL-致命")
    private String severity;

    @Schema(description = "是否启用")
    private Boolean enabled;

    @Schema(description = "责任部门")
    private String responsibleDept;

    @Schema(description = "创建人")
    private String createBy;

    // 移除了重复定义的 createTimeStart 和 createTimeEnd
    // 这些字段已经继承自 BaseQueryDTO

    @Schema(description = "关键词搜索（同时搜索代码和名称）")
    private String keyword;

    @Schema(description = "排序字段: code, name, type, severity, create_time")
    private String sortField = "defect_code";

    public DefectCodeQueryDTO() {
        // 默认每页显示100条，因为缺陷代码通常不会太多
        this.setPageSize(100);
    }

    /**
     * 获取查询条件的简要描述
     */
    public String getQueryDescription() {
        StringBuilder sb = new StringBuilder();
        if (defectCode != null) {
            sb.append("缺陷代码:").append(defectCode).append(" ");
        }
        if (defectName != null) {
            sb.append("缺陷名称:").append(defectName).append(" ");
        }
        if (defectType != null) {
            sb.append("缺陷类型:").append(defectType).append(" ");
        }
        if (severity != null) {
            sb.append("严重程度:").append(severity).append(" ");
        }
        if (enabled != null) {
            sb.append("启用状态:").append(enabled ? "启用" : "禁用").append(" ");
        }
        return sb.toString();
    }

    /**
     * 验证查询参数
     */
    public void validateQuery() {
        super.validate();

        // 验证时间范围（使用继承自父类的字段）
        if (super.getCreateTimeStart() != null && super.getCreateTimeEnd() != null) {
            if (super.getCreateTimeStart().isAfter(super.getCreateTimeEnd())) {
                throw new IllegalArgumentException("创建开始时间不能晚于结束时间");
            }
        }

        // 处理关键词搜索
        if (keyword != null && !keyword.trim().isEmpty()) {
            this.defectCode = keyword;
            this.defectName = keyword;
        }
    }

    /**
     * 构建启用缺陷代码查询
     */
    public static DefectCodeQueryDTO enabledQuery() {
        DefectCodeQueryDTO query = new DefectCodeQueryDTO();
        query.setEnabled(true);
        query.setOrderBy("defect_code");
        query.setOrderDirection("ASC");
        return query;
    }

    /**
     * 构建严重缺陷查询
     */
    public static DefectCodeQueryDTO criticalDefectQuery() {
        DefectCodeQueryDTO query = new DefectCodeQueryDTO();
        query.setSeverity("CRITICAL,FATAL");
        query.setEnabled(true);
        query.setOrderBy("defect_type, defect_code");
        query.setOrderDirection("ASC");
        return query;
    }

    /**
     * 构建按类型查询
     */
    public static DefectCodeQueryDTO byTypeQuery(String defectType) {
        DefectCodeQueryDTO query = new DefectCodeQueryDTO();
        query.setDefectType(defectType);
        query.setEnabled(true);
        query.setOrderBy("defect_code");
        query.setOrderDirection("ASC");
        return query;
    }

    /**
     * 检查是否需要模糊搜索
     */
    public boolean isFuzzySearch() {
        return (defectCode != null && defectCode.contains("*")) ||
                (defectName != null && defectName.contains("*")) ||
                (keyword != null && keyword.contains("*"));
    }

    /**
     * 获取SQL LIKE模式的搜索词
     */
    public String getLikePattern(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("*", "%");
    }
}
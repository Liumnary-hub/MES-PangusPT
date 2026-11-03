package com.mes.dto;

import com.mes.dto.BaseQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 检验标准查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StandardQueryDTO extends BaseQueryDTO {

    private String standardCode;          // 标准编号
    private String standardName;          // 标准名称
    private String productCode;          // 产品编码
    private String productName;          // 产品名称
    private String inspectionType;       // 检验类型: FIRST, PATROL, FINAL, SPOT
    private String status;               // 状态: DRAFT, ACTIVE, INACTIVE, DELETED
    private Boolean includeItemCount = false;  // 是否包含项目数量统计

    private String createBy;             // 创建人
    private String updateBy;             // 更新人
    private String startDate;            // 开始日期
    private String endDate;              // 结束日期
    private Long productId;              // 产品ID

    // 添加用于查询生效日期的字段
    private Boolean effectiveOnly;       // 是否只查询生效标准
    private String effectiveDateStart;   // 生效开始日期
    private String effectiveDateEnd;     // 生效结束日期

    // 是否包含检验项目详情
    private Boolean includeItems = false;

    // 版本号查询
    private String version;

    // 是否关键标准
    private Boolean isCritical;

    /**
     * 获取是否包含项目数量统计
     */
    public boolean isIncludeItemCount() {
        return Boolean.TRUE.equals(includeItemCount);
    }

    /**
     * 获取是否包含检验项目详情
     */
    public boolean isIncludeItems() {
        return Boolean.TRUE.equals(includeItems);
    }
}
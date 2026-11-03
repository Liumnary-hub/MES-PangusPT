package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mes.dto.BaseQueryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 检验计划查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "检验计划查询参数")
public class PlanQueryDTO extends BaseQueryDTO {

    @Schema(description = "计划编号")
    private String planNo;

    @Schema(description = "计划名称")
    private String planName;

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "计划类型: first, patrol, final, spot")
    private String planType;

    @Schema(description = "状态: pending, in_progress, completed, cancelled, draft, approved")
    private String status;

    @Schema(description = "检验员")
    private String inspector;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "计划开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planDateStart;

    @Schema(description = "计划结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate planDateEnd;

    @Schema(description = "创建开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTimeEnd;

    @Schema(description = "审批开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTimeStart;

    @Schema(description = "审批结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvalTimeEnd;

    @Schema(description = "是否包含明细", defaultValue = "false")
    private Boolean includeItems = false;

    @Schema(description = "是否紧急")
    private Boolean urgent;

    @Schema(description = "标准编号")
    private String standardCode;

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "产线编码")
    private String lineCode;

    @Schema(description = "是否过期")
    private Boolean expired;

    public PlanQueryDTO() {
        // 默认查询最近30天的数据
        if (this.planDateStart == null) {
            this.planDateStart = LocalDate.now().minusDays(30);
        }
        if (this.planDateEnd == null) {
            this.planDateEnd = LocalDate.now();
        }
    }

    /**
     * 验证查询参数
     */
    public void validateQuery() {
        super.validate();

        // 验证日期范围
        if (planDateStart != null && planDateEnd != null) {
            if (planDateStart.isAfter(planDateEnd)) {
                throw new IllegalArgumentException("计划开始日期不能晚于结束日期");
            }
        }

        if (createTimeStart != null && createTimeEnd != null) {
            if (createTimeStart.isAfter(createTimeEnd)) {
                throw new IllegalArgumentException("创建开始时间不能晚于结束时间");
            }
        }
    }

    /**
     * 构建待审批计划查询
     */
    public static PlanQueryDTO pendingApprovalQuery() {
        PlanQueryDTO query = new PlanQueryDTO();
        query.setStatus("draft");
        query.setOrderBy("create_time");
        query.setOrderDirection("ASC");
        return query;
    }

    /**
     * 构建今日计划查询
     */
    public static PlanQueryDTO todayPlanQuery() {
        PlanQueryDTO query = new PlanQueryDTO();
        query.setPlanDateStart(LocalDate.now());
        query.setPlanDateEnd(LocalDate.now());
        query.setStatus("approved");
        query.setOrderBy("plan_type, sequence");
        query.setOrderDirection("ASC");
        return query;
    }

    /**
     * 构建逾期计划查询
     */
    public static PlanQueryDTO overduePlanQuery() {
        PlanQueryDTO query = new PlanQueryDTO();
        query.setPlanDateEnd(LocalDate.now().minusDays(1));
        query.setStatus("approved");
        query.setExpired(true);
        query.setOrderBy("plan_date");
        query.setOrderDirection("ASC");
        return query;
    }
}
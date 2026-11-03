package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 检验任务查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "检验任务查询参数")
public class TaskQueryDTO extends BaseQueryDTO {

    @Schema(description = "任务编号")
    private String taskNo;

    @Schema(description = "计划编号")
    private String planNo;

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "生产订单号")
    private String orderNo;

    @Schema(description = "工位编码")
    private String workstationCode;

    @Schema(description = "检验员")
    private String inspector;

    @Schema(description = "状态: PENDING-待检, INSPECTING-检验中, COMPLETED-已完成, CANCELLED-已取消")
    private String status;

    @Schema(description = "检验结果: QUALIFIED-合格, CONDITIONAL-条件接收, REJECTED-拒收")
    private String result;

    @Schema(description = "检验类型: FIRST-首件检验, PATROL-巡检, FINAL-末件检验, SPOT-抽检")
    private String inspectionType;

    @Schema(description = "是否紧急")
    private Boolean urgent;

    @Schema(description = "检验开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionTimeStart;

    @Schema(description = "检验结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inspectionTimeEnd;

    @Schema(description = "完成开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTimeStart;

    @Schema(description = "完成结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completeTimeEnd;

    @Schema(description = "开始日期（简化的日期查询）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "结束日期（简化的日期查询）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "是否包含检验记录")
    private Boolean includeRecords = false;

    @Schema(description = "是否包含缺陷明细")
    private Boolean includeDefects = false;

    @Schema(description = "车间编码")
    private String workshopCode;

    @Schema(description = "产线编码")
    private String lineCode;

    @Schema(description = "是否超时")
    private Boolean timeout;

    @Schema(description = "排序方式: DEFAULT-默认排序, URGENT-紧急优先, TIMEOUT-超时优先")
    private String sortMode = "DEFAULT";

    /**
     * 默认构造器，设置默认时间范围（最近7天）
     */
    public TaskQueryDTO() {
        // 默认查询最近7天的数据
        if (this.startDate == null) {
            this.startDate = LocalDate.now().minusDays(7);
        }
        if (this.endDate == null) {
            this.endDate = LocalDate.now();
        }
    }

    /**
     * 获取查询条件的简要描述
     */
    public String getQueryDescription() {
        StringBuilder sb = new StringBuilder();
        if (taskNo != null) {
            sb.append("任务编号:").append(taskNo).append(" ");
        }
        if (productCode != null) {
            sb.append("产品编码:").append(productCode).append(" ");
        }
        if (status != null) {
            sb.append("状态:").append(status).append(" ");
        }
        if (inspector != null) {
            sb.append("检验员:").append(inspector).append(" ");
        }
        return sb.toString();
    }

    /**
     * 验证查询参数
     */
    public void validateQuery() {
        super.validate();

        // 验证时间范围（现在使用继承自父类的时间字段）
        if (super.getCreateTimeStart() != null && super.getCreateTimeEnd() != null) {
            if (super.getCreateTimeStart().isAfter(super.getCreateTimeEnd())) {
                throw new IllegalArgumentException("创建开始时间不能晚于结束时间");
            }
        }

        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("开始日期不能晚于结束日期");
            }
        }
    }

    /**
     * 构建待检任务查询
     */
    public static TaskQueryDTO pendingTasksQuery() {
        TaskQueryDTO query = new TaskQueryDTO();
        query.setStatus("PENDING");
        query.setOrderBy("create_time");
        query.setOrderDirection("ASC");
        return query;
    }

    /**
     * 构建我的任务查询
     */
    public static TaskQueryDTO myTasksQuery(String inspector) {
        TaskQueryDTO query = new TaskQueryDTO();
        query.setInspector(inspector);
        query.setStatus("PENDING,INSPECTING");
        query.setOrderBy("urgent DESC, create_time ASC");
        return query;
    }

    /**
     * 构建超时任务查询
     */
    public static TaskQueryDTO timeoutTasksQuery() {
        TaskQueryDTO query = new TaskQueryDTO();
        query.setTimeout(true);
        query.setStatus("PENDING,INSPECTING");
        query.setOrderBy("create_time ASC");
        return query;
    }

    /**
     * 构建统计查询
     */
    public static TaskQueryDTO statisticQuery(LocalDate startDate, LocalDate endDate) {
        TaskQueryDTO query = new TaskQueryDTO();
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        query.setStatus("COMPLETED");
        query.setCountTotal(true);
        return query;
    }
}
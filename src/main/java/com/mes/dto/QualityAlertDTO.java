package com.mes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Schema(description = "质量预警参数")
public class QualityAlertDTO {

    @Schema(description = "预警ID")
    private Long id;

    @Schema(description = "预警编号")
    private String alertNo;

    @Schema(description = "预警类型: DEFECT_RATE-缺陷率预警, SPC_ABNORMAL-SPC异常, PATROL_ABNORMAL-巡检异常, DEADLINE-期限预警")
    @NotBlank(message = "预警类型不能为空")
    private String alertType;

    @Schema(description = "预警级别: INFO-信息, WARNING-警告, ERROR-错误, CRITICAL-严重")
    @NotBlank(message = "预警级别不能为空")
    private String alertLevel;

    @Schema(description = "预警标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "预警标题不能为空")
    private String alertTitle;

    @Schema(description = "预警内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "预警内容不能为空")
    private String alertContent;

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "产品名称")
    private String productName;

    @Schema(description = "批次号")
    private String batchNo;

    @Schema(description = "检验任务ID")
    private Long taskId;

    @Schema(description = "检验任务编号")
    private String taskNo;

    @Schema(description = "巡检记录ID")
    private Long patrolId;

    @Schema(description = "相关数据")
    private String relatedData;

    @Schema(description = "预警触发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime triggerTime;

    @Schema(description = "预警状态: UNREAD-未读, READ-已读, PROCESSING-处理中, RESOLVED-已解决, CLOSED-已关闭")
    private String status = "UNREAD";

    @Schema(description = "处理人")
    private String handler;

    @Schema(description = "处理时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    private String handleResult;

    @Schema(description = "处理意见")
    private String handleComment;

    @Schema(description = "关闭时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime closeTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 判断是否需要紧急处理
     */
    public boolean isUrgent() {
        return "ERROR".equals(alertLevel) || "CRITICAL".equals(alertLevel);
    }

    /**
     * 判断是否已处理
     */
    public boolean isHandled() {
        return "RESOLVED".equals(status) || "CLOSED".equals(status);
    }
}
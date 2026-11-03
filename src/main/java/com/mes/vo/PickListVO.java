package com.mes.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PickListVO {
    private Long id;
    private String pickListCode;
    private Long workOrderId;
    private String workOrderCode;
    private Long taskId;
    private String taskCode;
    private String status;
    private String statusDesc;
    private String pickerId;
    private String pickerName;
    private LocalDateTime pickTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createUser;
    private String updateUser;

    // 新增的关联字段
    private String productName;        // 产品名称
    private Integer plannedQuantity;   // 计划数量

    private List<PickListDetailVO> details;

    // 统计信息
    private Integer totalItems;        // 总项数
    private Integer pickedItems;       // 已拣项数
    private BigDecimal totalRequired;  // 总需求数量
    private BigDecimal totalPicked;    // 总实拣数量

    // 状态描述方法
    public String getStatusDesc() {
        if (status == null) return "";
        switch (status) {
            case "pending": return "待领料";
            case "picking": return "领料中";
            case "completed": return "已完成";
            case "cancelled": return "已取消";
            default: return status;
        }
    }
}
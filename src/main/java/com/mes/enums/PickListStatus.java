package com.mes.enums;

/**
 * 领料单状态枚举
 */
public enum PickListStatus {
    PENDING("待领料"),
    PICKING("领料中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");

    private final String description;

    PickListStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

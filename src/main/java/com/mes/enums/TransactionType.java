package com.mes.enums;

/**
 * 库存交易类型枚举
 */
public enum TransactionType {
    IN("入库"),
    OUT("出库"),
    TRANSFER("转移"),
    ADJUST("调整");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

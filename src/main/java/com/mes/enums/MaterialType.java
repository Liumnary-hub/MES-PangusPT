package com.mes.enums;

/**
 * 物料类型枚举
 */
public enum MaterialType {
    RAW("原材料"),
    SEMI("半成品"),
    PROD("成品"),
    TOOL("工装"),
    CONSUMABLE("消耗品");

    private final String description;

    MaterialType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}


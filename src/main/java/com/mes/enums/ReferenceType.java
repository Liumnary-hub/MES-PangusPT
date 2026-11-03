package com.mes.enums;

public enum ReferenceType {
    WORK_ORDER("工单"),
    PICK_LIST("领料单"),
    ADJUSTMENT("调整"),
    TRANSFER("转移"),
    RETURN("退料"),
    SCRAP("报废"),
    CHECK("盘点"),
    PRODUCTION("生产投料"),
    FINISHED("完工入库");

    private final String description;

    ReferenceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
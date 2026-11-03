package com.mes.vo;

import lombok.Data;

@Data
public class ProcessCapabilityIndex {
    private Double cp;                  // 过程能力指数
    private Double cpk;                 // 过程能力指数（考虑偏移）
    private Double cpu;                 // 上单侧过程能力指数
    private Double cpl;                 // 下单侧过程能力指数
    private Double pp;                  // 过程性能指数
    private Double ppk;                 // 过程性能指数（考虑偏移）
    private Double ppu;                 // 上单侧过程性能指数
    private Double ppl;                 // 下单侧过程性能指数
    private Double stdDev;              // 标准差
    private Double mean;                // 均值
    private Double usl;                 // 上规格限
    private Double lsl;                 // 下规格限
    private Double target;              // 目标值

    // 能力评估
    private String cpLevel;             // Cp水平评估
    private String cpkLevel;            // Cpk水平评估
    private Boolean isCapable;          // 是否具备过程能力
    private String capabilityClass;     // 能力等级
}

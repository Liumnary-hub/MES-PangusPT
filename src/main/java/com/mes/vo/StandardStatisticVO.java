package com.mes.vo;

import lombok.Data;

import java.util.Map;

@Data
public class StandardStatisticVO {
    private Long totalCount;           // 标准总数
    private Long activeCount;          // 有效标准数
    private Long draftCount;           // 草稿标准数
    private Long expiredCount;         // 过期标准数
    private Map<String, Long> typeDistribution;  // 按检验类型分布
    private Map<String, Long> productDistribution; // 按产品分布
}

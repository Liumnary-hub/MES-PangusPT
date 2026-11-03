package com.mes.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SPCResultVO {
    // 控制图数据
    private Map<String, List<SPCDataPoint>> controlCharts; // 控制图数据

    // 过程能力指数
    private ProcessCapabilityIndex capabilityIndex;        // 过程能力指数

    // 异常检测
    private List<SPCAbnormalPoint> abnormalPoints;         // 异常点
    private Integer totalAbnormalPoints;                   // 异常点总数

    // 分析结论
    private String conclusion;                             // 分析结论
    private List<String> suggestions;                      // 改进建议

    // 统计参数
    private Integer subgroupSize;                          // 子组大小
    private Integer totalSubgroups;                        // 总子组数
    private Integer totalSamples;                          // 总样本数
}




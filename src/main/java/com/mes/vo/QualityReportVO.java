package com.mes.vo;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class QualityReportVO {
    private String reportNo;            // 报告编号
    private String reportTitle;         // 报告标题
    private String reportType;          // 报告类型（日报/周报/月报/专项报告）
    private String period;              // 报告周期

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime generateTime;  // 生成时间

    private String generateBy;          // 生成人
    private String department;          // 部门
    private String productLine;         // 产品线

    // 质量概况
    private QualityDashboardVO dashboardData;   // 看板数据

    // 分析结果
    private SPCResultVO spcResult;              // SPC分析结果
    private ParetoAnalysisVO paretoResult;      // 帕累托分析结果

    // 专项分析
    private String issueAnalysis;               // 问题分析
    private List<String> keyIssues;             // 关键问题
    private List<String> improvementActions;    // 改进措施
    private List<String> suggestions;           // 改进建议

    // 统计汇总
    private Long totalTasks;                    // 总任务数
    private Long totalInspections;              // 总检验数
    private Long totalDefects;                  // 总缺陷数
    private Double overallPassRate;             // 总体合格率
    private Double averageDefectRate;           // 平均缺陷率

    // 趋势分析
    private String qualityTrend;                // 质量趋势描述
    private String performanceAssessment;       // 绩效评估
    private String riskAssessment;              // 风险评估

    // 结论与建议
    private String conclusion;                  // 结论
    private String recommendations;             // 建议
    private List<String> followUpActions;       // 后续行动计划
}
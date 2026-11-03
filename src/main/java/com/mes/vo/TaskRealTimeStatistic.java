package com.mes.vo;


import com.mes.entity.QualityTask;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TaskRealTimeStatistic {
    // 今日统计
    private Long todayTotalTasks;      // 今日任务总数
    private Long todayPendingTasks;    // 今日待检任务
    private Long todayInspectingTasks; // 今日检验中任务
    private Long todayCompletedTasks;  // 今日完成任务
    private Long todayQualifiedTasks;  // 今日合格任务
    private Long todayRejectedTasks;   // 今日不合格任务
    private Double todayPassRate;      // 今日合格率

    // 实时数据
    private List<QualityTask> inspectingTasks;  // 正在检验的任务
    private List<QualityTask> timeoutTasks;     // 超时任务

    // 统计图表数据
    private List<Map<String, Object>> statusDistribution;  // 状态分布
    private List<Map<String, Object>> resultDistribution;  // 结果分布
    private List<Map<String, Object>> inspectorWorkload;   // 检验员工作量
    private List<Map<String, Object>> defectDistribution;  // 缺陷分布

    // 趋势分析
    private List<Map<String, Object>> hourlyTrend;         // 小时趋势
    private List<Map<String, Object>> passRateTrend;       // 合格率趋势
    private List<Map<String, Object>> defectTrend;         // 缺陷趋势

    // TOP统计
    private List<Map<String, Object>> topDefects;          // TOP缺陷
    private List<Map<String, Object>> topProducts;         // TOP产品
    private List<Map<String, Object>> topWorkstations;     // TOP工位
}


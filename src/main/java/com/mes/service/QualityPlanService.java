package com.mes.service;

import com.mes.common.PageResult;
import com.mes.dto.PlanCreateDTO;
import com.mes.dto.PlanExecuteDTO;
import com.mes.dto.PlanQueryDTO;
import com.mes.dto.PlanUpdateDTO;
import com.mes.entity.QualityPlan;
import com.mes.vo.PlanDetailVO;
import com.mes.vo.PlanStatisticVO;
import com.mes.vo.PlanVO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface QualityPlanService {

    // 计划管理
    PageResult<PlanVO> getPlanList(PlanQueryDTO query);
    PlanDetailVO getPlanDetail(Long id);
    Long createPlan(PlanCreateDTO dto);
    void updatePlan(PlanUpdateDTO dto);
    void deletePlan(Long id);
    void deleteBatch(List<Long> ids);

    // 计划流程
    void approvePlan(Long id, String approver, String remark);
    void cancelPlan(Long id, String reason);
    void suspendPlan(Long id, String reason);
    void resumePlan(Long id);
    void completePlan(Long id);

    // 执行管理
    List<Long> executePlan(PlanExecuteDTO dto);
    void schedulePlan(Long id, LocalDateTime scheduledTime);
    void reschedulePlan(Long id, LocalDateTime newScheduledTime);

    // 查询接口
    List<QualityPlan> getPlansByProduct(Long productId);
    List<QualityPlan> getPlansByStandard(Long standardId);
    List<QualityPlan> getTodayPlans();
    List<QualityPlan> getOverduePlans();
    List<QualityPlan> getPendingPlans();
    List<QualityPlan> getActivePlans();

    // 统计分析
    PlanStatisticVO getPlanStatistics(PlanQueryDTO query);
    Map<String, Long> getPlanCountByStatus();
    Map<String, Long> getPlanCountByType();

    // 监控提醒
    void checkAndAlertOverduePlans();
    void sendPlanReminder(Long planId);
    Map<String, Object> getPlanProgress(Long planId);
}
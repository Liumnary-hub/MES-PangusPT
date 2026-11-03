package com.mes.service.impl;// QualityPlanServiceImpl.java - 实现


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.common.PageResult;
import com.mes.common.BusinessException;
import com.mes.common.ValidationException;
import com.mes.dto.PlanQueryDTO;
import com.mes.dto.PlanCreateDTO;
import com.mes.dto.PlanUpdateDTO;
import com.mes.dto.PlanExecuteDTO;
import com.mes.entity.QualityPlan;
import com.mes.entity.QualityPlanItem;
import com.mes.entity.QualityStandard;
import com.mes.mapper.QualityPlanMapper;
import com.mes.mapper.QualityPlanItemMapper;
import com.mes.mapper.QualityStandardMapper;
import com.mes.service.QualityPlanService;
import com.mes.service.QualityTaskService;
import com.mes.vo.PlanDetailVO;
import com.mes.vo.PlanStatisticVO;
import com.mes.vo.PlanVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class QualityPlanServiceImpl implements QualityPlanService {

    private final QualityPlanMapper planMapper;
    private final QualityPlanItemMapper planItemMapper;
    private final QualityStandardMapper standardMapper;
    private final QualityTaskService taskService;

    @Override
    public PageResult<PlanVO> getPlanList(PlanQueryDTO query) {
        try {
            validateQuery(query);

            Page<QualityPlan> page = new Page<>(query.getPageNum(), query.getPageSize());
            IPage<QualityPlan> planPage = planMapper.selectPageList(page, query);

            // 转换为VO
            List<PlanVO> voList = planPage.getRecords().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());

            // 如果包含明细，查询明细数据
            if (query.getIncludeItems() != null && query.getIncludeItems()) {
                for (PlanVO vo : voList) {
                    List<QualityPlanItem> items = planItemMapper.selectByPlanId(vo.getId());
                    vo.setItems(items);
                }
            }

            return PageResult.of(voList, planPage.getTotal(),
                    query.getPageNum(), query.getPageSize());

        } catch (Exception e) {
            log.error("查询检验计划列表失败", e);
            throw new BusinessException("查询检验计划列表失败: " + e.getMessage());
        }
    }

    @Override
    public PlanDetailVO getPlanDetail(Long id) {
        try {
            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            PlanDetailVO detail = new PlanDetailVO();
            BeanUtils.copyProperties(plan, detail);

            // 查询计划明细
            List<QualityPlanItem> items = planItemMapper.selectByPlanId(id);
            detail.setItems(items);

            // 查询标准信息
            if (plan.getStandardId() != null) {
                QualityStandard standard = standardMapper.selectById(plan.getStandardId());
                if (standard != null) {
                    detail.setStandardCode(standard.getStandardCode());
                    detail.setStandardName(standard.getStandardName());
                }
            }

            return detail;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取检验计划详情失败，ID: {}", id, e);
            throw new BusinessException("获取检验计划详情失败: " + e.getMessage());
        }
    }

    @Override
    public Long createPlan(PlanCreateDTO dto) {
        try {
            validateCreateDTO(dto);

            // 验证标准是否存在
            QualityStandard standard = standardMapper.selectByStandardCode(dto.getStandardCode());
            if (standard == null) {
                throw new ValidationException("检验标准不存在");
            }

            // 检查标准状态
            if (!"ACTIVE".equals(standard.getStatus())) {
                throw new ValidationException("检验标准未生效");
            }

            // 生成计划编号
            String planNo = generatePlanNo(dto.getPlanType());

            // 创建计划
            QualityPlan plan = new QualityPlan();
            BeanUtils.copyProperties(dto, plan);
            plan.setPlanNo(planNo);
            plan.setStandardId(standard.getId());
            plan.setStatus("DRAFT");
            plan.setCreateTime(LocalDateTime.now());
            plan.setUpdateTime(LocalDateTime.now());
            plan.setDeleted(0);

            planMapper.insert(plan);

            // 保存计划明细
            if (!CollectionUtils.isEmpty(dto.getItems())) {
                savePlanItems(plan.getId(), standard.getId(), dto.getItems());
            }

            log.info("创建检验计划成功，计划编号: {}", planNo);
            return plan.getId();

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("创建检验计划失败", e);
            throw new BusinessException("创建检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void updatePlan(PlanUpdateDTO dto) {
        try {
            validateUpdateDTO(dto);

            QualityPlan plan = planMapper.selectById(dto.getId());
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 检查状态，已审批或执行的计划不能修改
            if (!"DRAFT".equals(plan.getStatus()) && !"PENDING".equals(plan.getStatus())) {
                throw new BusinessException("计划已审批或执行，不能修改");
            }

            // 更新计划信息
            BeanUtils.copyProperties(dto, plan, "planNo", "standardId");
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            // 更新计划明细
            if (dto.getItems() != null) {
                planItemMapper.deleteByPlanId(dto.getId());
                if (!dto.getItems().isEmpty()) {
                    savePlanItems(plan.getId(), plan.getStandardId(), dto.getItems());
                }
            }

            log.info("更新检验计划成功，计划ID: {}", dto.getId());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新检验计划失败，ID: {}", dto.getId(), e);
            throw new BusinessException("更新检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void deletePlan(Long id) {
        try {
            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 检查状态，已执行的计划不能删除
            if ("EXECUTED".equals(plan.getStatus()) || "EXECUTING".equals(plan.getStatus())) {
                throw new BusinessException("计划已执行，不能删除");
            }

            // 逻辑删除
            plan.setDeleted(1);
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("删除检验计划成功，计划ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("删除检验计划失败，ID: {}", id, e);
            throw new BusinessException("删除检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        try {
            if (CollectionUtils.isEmpty(ids)) {
                throw new ValidationException("请选择要删除的计划");
            }

            // 检查每个计划是否可删除
            List<QualityPlan> plans = planMapper.selectBatchIds(ids);
            for (QualityPlan plan : plans) {
                if ("EXECUTED".equals(plan.getStatus()) || "EXECUTING".equals(plan.getStatus())) {
                    throw new BusinessException("计划 " + plan.getPlanNo() + " 已执行，不能删除");
                }
            }

            // 批量逻辑删除
            planMapper.deleteBatchByIds(ids);

            log.info("批量删除检验计划成功，数量: {}", ids.size());

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("批量删除检验计划失败", e);
            throw new BusinessException("批量删除检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void approvePlan(Long id, String approver, String remark) {
        try {
            if (!StringUtils.hasText(approver)) {
                throw new ValidationException("审批人不能为空");
            }

            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 只能审批草稿状态的计划
            if (!"DRAFT".equals(plan.getStatus())) {
                throw new BusinessException("只有草稿状态的计划可以审批");
            }

            // 检查是否有计划明细
            Long itemCount = planItemMapper.countByPlanId(id);
            if (itemCount == 0) {
                throw new BusinessException("请先添加计划明细");
            }

            // 更新状态
            plan.setStatus("APPROVED");
            plan.setApprovalBy(approver);
            plan.setApprovalTime(LocalDateTime.now());
            plan.setRemark(remark);
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("审批检验计划成功，计划ID: {}，审批人: {}", id, approver);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("审批检验计划失败，ID: {}", id, e);
            throw new BusinessException("审批检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void cancelPlan(Long id, String reason) {
        try {
            if (!StringUtils.hasText(reason)) {
                throw new ValidationException("取消原因不能为空");
            }

            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 只能取消草稿或已审批状态的计划
            if (!"DRAFT".equals(plan.getStatus()) && !"APPROVED".equals(plan.getStatus())) {
                throw new BusinessException("当前状态的计划不能取消");
            }

            plan.setStatus("CANCELLED");
            plan.setRemark(reason);
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("取消检验计划成功，计划ID: {}，原因: {}", id, reason);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("取消检验计划失败，ID: {}", id, e);
            throw new BusinessException("取消检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void suspendPlan(Long id, String reason) {
        try {
            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 只能暂停执行中的计划
            if (!"EXECUTING".equals(plan.getStatus())) {
                throw new BusinessException("只有执行中的计划可以暂停");
            }

            plan.setStatus("SUSPENDED");
            plan.setRemark(reason);
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("暂停检验计划成功，计划ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("暂停检验计划失败，ID: {}", id, e);
            throw new BusinessException("暂停检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void resumePlan(Long id) {
        try {
            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 只能恢复暂停的计划
            if (!"SUSPENDED".equals(plan.getStatus())) {
                throw new BusinessException("只有暂停的计划可以恢复");
            }

            plan.setStatus("EXECUTING");
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("恢复检验计划成功，计划ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("恢复检验计划失败，ID: {}", id, e);
            throw new BusinessException("恢复检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void completePlan(Long id) {
        try {
            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 只能完成执行中的计划
            if (!"EXECUTING".equals(plan.getStatus())) {
                throw new BusinessException("只有执行中的计划可以完成");
            }

            plan.setStatus("COMPLETED");
            plan.setCompleteTime(LocalDateTime.now());
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("完成检验计划成功，计划ID: {}", id);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("完成检验计划失败，ID: {}", id, e);
            throw new BusinessException("完成检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<Long> executePlan(PlanExecuteDTO dto) {
        try {
            validateExecuteDTO(dto);

            QualityPlan plan = planMapper.selectById(dto.getPlanId());
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            // 检查计划状态
            if (!"APPROVED".equals(plan.getStatus()) && !"SCHEDULED".equals(plan.getStatus())) {
                throw new BusinessException("只有已审批或已调度的计划可以执行");
            }

            // 检查计划日期
            if (plan.getPlanDate() != null && plan.getPlanDate().isAfter(LocalDateTime.now())) {
                throw new BusinessException("计划日期未到，不能执行");
            }

            // TODO: 调用任务服务生成检验任务
            List<Long> taskIds = new ArrayList<>();
            // taskIds = taskService.generateTasksFromPlan(plan, dto);

            // 更新计划状态
            plan.setStatus("EXECUTING");
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("执行检验计划成功，计划ID: {}，生成任务数量: {}", dto.getPlanId(), taskIds.size());
            return taskIds;

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("执行检验计划失败，计划ID: {}", dto.getPlanId(), e);
            throw new BusinessException("执行检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void schedulePlan(Long id, LocalDateTime scheduledTime) {
        try {
            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            if (!"APPROVED".equals(plan.getStatus())) {
                throw new BusinessException("只有已审批的计划可以调度");
            }

            plan.setStatus("SCHEDULED");
            plan.setScheduledTime(scheduledTime);
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("调度检验计划成功，计划ID: {}，调度时间: {}", id, scheduledTime);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("调度检验计划失败，ID: {}", id, e);
            throw new BusinessException("调度检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public void reschedulePlan(Long id, LocalDateTime newScheduledTime) {
        try {
            QualityPlan plan = planMapper.selectById(id);
            if (plan == null) {
                throw new BusinessException("检验计划不存在");
            }

            if (!"SCHEDULED".equals(plan.getStatus())) {
                throw new BusinessException("只有已调度的计划可以重新调度");
            }

            plan.setScheduledTime(newScheduledTime);
            plan.setUpdateTime(LocalDateTime.now());
            planMapper.updateById(plan);

            log.info("重新调度检验计划成功，计划ID: {}，新调度时间: {}", id, newScheduledTime);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("重新调度检验计划失败，ID: {}", id, e);
            throw new BusinessException("重新调度检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityPlan> getPlansByProduct(Long productId) {
        try {
            return planMapper.selectByProductId(productId);
        } catch (Exception e) {
            log.error("获取产品检验计划失败，产品ID: {}", productId, e);
            throw new BusinessException("获取产品检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityPlan> getPlansByStandard(Long standardId) {
        try {
            return planMapper.selectByStandardId(standardId);
        } catch (Exception e) {
            log.error("获取标准检验计划失败，标准ID: {}", standardId, e);
            throw new BusinessException("获取标准检验计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityPlan> getTodayPlans() {
        try {
            return planMapper.selectTodayPlans();
        } catch (Exception e) {
            log.error("获取今日计划失败", e);
            throw new BusinessException("获取今日计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityPlan> getOverduePlans() {
        try {
            return planMapper.selectOverduePlans();
        } catch (Exception e) {
            log.error("获取逾期计划失败", e);
            throw new BusinessException("获取逾期计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityPlan> getPendingPlans() {
        try {
            return planMapper.selectPendingPlans();
        } catch (Exception e) {
            log.error("获取待处理计划失败", e);
            throw new BusinessException("获取待处理计划失败: " + e.getMessage());
        }
    }

    @Override
    public List<QualityPlan> getActivePlans() {
        try {
            return planMapper.selectActivePlans();
        } catch (Exception e) {
            log.error("获取活跃计划失败", e);
            throw new BusinessException("获取活跃计划失败: " + e.getMessage());
        }
    }

    @Override
    public PlanStatisticVO getPlanStatistics(PlanQueryDTO query) {
        try {
            PlanStatisticVO statistic = new PlanStatisticVO();

            // 统计各状态计划数量
            Map<String, Long> statusCount = planMapper.countByStatus(query);
            statistic.setStatusCount(statusCount);

            // 统计月度趋势
            List<Map<String, Object>> monthlyTrend = planMapper.getMonthlyTrend(query);
            statistic.setMonthlyTrend(monthlyTrend);

            // 统计产品维度
            List<Map<String, Object>> productStats = planMapper.getProductStatistics(query);
            statistic.setProductStats(productStats);

            // 计算执行率
            long totalPlans = statusCount.values().stream().mapToLong(Long::longValue).sum();
            long executedPlans = statusCount.getOrDefault("EXECUTED", 0L) +
                    statusCount.getOrDefault("EXECUTING", 0L);
            double executionRate = totalPlans > 0 ? (double) executedPlans / totalPlans * 100 : 0;
            statistic.setExecutionRate(executionRate);

            return statistic;

        } catch (Exception e) {
            log.error("获取计划统计失败", e);
            throw new BusinessException("获取计划统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getPlanCountByStatus() {
        try {
            return planMapper.countByStatus(null);
        } catch (Exception e) {
            log.error("获取计划状态统计失败", e);
            throw new BusinessException("获取计划状态统计失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Long> getPlanCountByType() {
        try {
            return planMapper.countByPlanType(null);
        } catch (Exception e) {
            log.error("获取计划类型统计失败", e);
            throw new BusinessException("获取计划类型统计失败: " + e.getMessage());
        }
    }

    @Override
    public void checkAndAlertOverduePlans() {
        try {
            List<QualityPlan> overduePlans = getOverduePlans();
            if (!overduePlans.isEmpty()) {
                for (QualityPlan plan : overduePlans) {
                    // TODO: 发送预警通知
                    log.warn("检测到逾期计划，计划ID: {}，计划编号: {}",
                            plan.getId(), plan.getPlanNo());
                }
            }
        } catch (Exception e) {
            log.error("检查逾期计划失败", e);
        }
    }

    @Override
    public void sendPlanReminder(Long planId) {
        try {
            QualityPlan plan = planMapper.selectById(planId);
            if (plan == null) {
                return;
            }

            // TODO: 实现计划提醒逻辑
            // 可以根据计划状态、时间等发送提醒

            log.info("发送计划提醒，计划ID: {}", planId);

        } catch (Exception e) {
            log.error("发送计划提醒失败，计划ID: {}", planId, e);
        }
    }

    @Override
    public Map<String, Object> getPlanProgress(Long planId) {
        try {
            Map<String, Object> progress = new HashMap<>();

            QualityPlan plan = planMapper.selectById(planId);
            if (plan == null) {
                return progress;
            }

            // TODO: 根据任务完成情况计算进度
            // 这里需要集成任务服务

            return progress;

        } catch (Exception e) {
            log.error("获取计划进度失败，计划ID: {}", planId, e);
            throw new BusinessException("获取计划进度失败: " + e.getMessage());
        }
    }

    // ============ 私有方法 ============

    private PlanVO convertToVO(QualityPlan plan) {
        PlanVO vo = new PlanVO();
        BeanUtils.copyProperties(plan, vo);

        // 计算任务完成情况
        // TODO: 根据实际情况计算

        return vo;
    }

    private void validateQuery(PlanQueryDTO query) {
        if (query == null) {
            throw new ValidationException("查询参数不能为空");
        }

        if (query.getPageNum() == null || query.getPageNum() < 1) {
            query.setPageNum(1);
        }

        if (query.getPageSize() == null || query.getPageSize() < 1) {
            query.setPageSize(20);
        }
    }

    private void validateCreateDTO(PlanCreateDTO dto) {
        if (dto == null) {
            throw new ValidationException("创建参数不能为空");
        }

        if (!StringUtils.hasText(dto.getPlanName())) {
            throw new ValidationException("计划名称不能为空");
        }

        if (!StringUtils.hasText(dto.getStandardCode())) {
            throw new ValidationException("标准编码不能为空");
        }

        if (!StringUtils.hasText(dto.getPlanType())) {
            throw new ValidationException("计划类型不能为空");
        }

        if (dto.getPlanDate() == null) {
            throw new ValidationException("计划日期不能为空");
        }
    }

    private void validateUpdateDTO(PlanUpdateDTO dto) {
        if (dto == null) {
            throw new ValidationException("更新参数不能为空");
        }

        if (dto.getId() == null) {
            throw new ValidationException("计划ID不能为空");
        }
    }

    private void validateExecuteDTO(PlanExecuteDTO dto) {
        if (dto == null) {
            throw new ValidationException("执行参数不能为空");
        }

        if (dto.getPlanId() == null) {
            throw new ValidationException("计划ID不能为空");
        }
    }

    private String generatePlanNo(String planType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateStr = LocalDateTime.now().format(formatter);
        String random = RandomStringUtils.randomNumeric(4);

        String prefix = "";
        switch (planType.toUpperCase()) {
            case "FIRST":
                prefix = "FJ";
                break;
            case "PATROL":
                prefix = "XJ";
                break;
            case "FINAL":
                prefix = "MJ";
                break;
            case "SPOT":
                prefix = "CJ";
                break;
            default:
                prefix = "JH";
        }

        return prefix + dateStr + random;
    }

    private void savePlanItems(Long planId, Long standardId,
                               List<com.mes.dto.PlanItemDTO> itemDTOs) {
        List<QualityPlanItem> items = new ArrayList<>();
        int sequence = 1;

        for (com.mes.dto.PlanItemDTO dto : itemDTOs) {
            QualityPlanItem item = new QualityPlanItem();
            item.setPlanId(planId);
            item.setStandardId(standardId);
            item.setItemId(dto.getItemId());
            item.setItemName(dto.getItemName());
            item.setSequence(sequence++);
            item.setRequired(dto.getRequired() != null ? dto.getRequired() : true);
            item.setCreateTime(LocalDateTime.now());
            item.setDeleted(0);

            items.add(item);
        }

        if (!items.isEmpty()) {
            planItemMapper.insertBatch(items);
        }
    }
}
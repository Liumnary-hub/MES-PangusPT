package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ProductionSchedule;
import com.mes.mapper.ProductionScheduleMapper;
import com.mes.service.ProductionScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ProductionScheduleServiceImpl extends ServiceImpl<ProductionScheduleMapper, ProductionSchedule>
        implements ProductionScheduleService {

    @Autowired
    private ProductionScheduleMapper productionScheduleMapper;

    @Override
    public IPage<ProductionSchedule> getSchedulePage(Page<ProductionSchedule> page,
                                                     String scheduleCode,
                                                     String scheduleName,
                                                     LocalDate scheduleDate,
                                                     String scheduleType,
                                                     String status) {
        QueryWrapper<ProductionSchedule> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0);

        if (StringUtils.isNotBlank(scheduleCode)) {
            queryWrapper.like("schedule_code", scheduleCode);
        }
        if (StringUtils.isNotBlank(scheduleName)) {
            queryWrapper.like("schedule_name", scheduleName);
        }
        if (scheduleDate != null) {
            queryWrapper.eq("schedule_date", scheduleDate);
        }
        if (StringUtils.isNotBlank(scheduleType)) {
            queryWrapper.eq("schedule_type", scheduleType);
        }
        if (StringUtils.isNotBlank(status)) {
            queryWrapper.eq("status", status);
        }

        queryWrapper.orderByDesc("create_time");

        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean createSchedule(ProductionSchedule schedule) {
        log.info("创建排产计划: {}", schedule.getScheduleCode());

        try {
            // 检查排产计划编号是否重复
            if (isScheduleCodeDuplicate(schedule.getScheduleCode(), null)) {
                throw new RuntimeException("排产计划编号已存在: " + schedule.getScheduleCode());
            }

            // 设置默认值
            if (schedule.getStatus() == null) {
                schedule.setStatus("DRAFT");
            }
            schedule.setCreateTime(LocalDateTime.now());
            schedule.setUpdateTime(LocalDateTime.now());

            boolean success = save(schedule);
            if (success) {
                log.info("排产计划创建成功, ID: {}", schedule.getId());
            }
            return success;

        } catch (Exception e) {
            log.error("创建排产计划失败: {}", schedule.getScheduleCode(), e);
            throw new RuntimeException("创建排产计划失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateScheduleStatus(Long id, String status) {
        log.info("更新排产计划状态, ID: {}, 新状态: {}", id, status);

        try {
            int result = productionScheduleMapper.updateStatus(id, status);
            boolean success = result > 0;

            if (success) {
                log.info("排产计划状态更新成功, ID: {}", id);
            } else {
                log.warn("排产计划状态更新失败, ID: {}", id);
            }
            return success;

        } catch (Exception e) {
            log.error("更新排产计划状态失败, ID: {}", id, e);
            throw new RuntimeException("更新排产计划状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean publishSchedule(Long id) {
        log.info("发布排产计划, ID: {}", id);
        return updateScheduleStatus(id, "PUBLISHED");
    }

    @Override
    @Transactional
    public boolean cancelSchedule(Long id) {
        log.info("取消排产计划, ID: {}", id);
        return updateScheduleStatus(id, "CANCELLED");
    }

    @Override
    public List<ProductionSchedule> getSchedulesByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("根据日期范围查询排产计划, startDate: {}, endDate: {}", startDate, endDate);
        return productionScheduleMapper.selectByDateRange(startDate, endDate);
    }

    @Override
    public boolean isScheduleCodeDuplicate(String scheduleCode, Long excludeId) {
        int count = productionScheduleMapper.countByScheduleCode(scheduleCode, excludeId);
        return count > 0;
    }

    @Override
    public ProductionSchedule getLatestSchedule() {
        return productionScheduleMapper.selectLatestSchedule();
    }

    @Override
    public List<ProductionSchedule> getSchedulesByOrderId(Long orderId) {
        log.info("根据工单ID查询排产计划, orderId: {}", orderId);
        return productionScheduleMapper.selectByOrderId(orderId);
    }
}
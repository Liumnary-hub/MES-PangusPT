package com.mes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ScheduleDetail;
import com.mes.mapper.ScheduleDetailMapper;
import com.mes.service.ScheduleDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ScheduleDetailServiceImpl extends ServiceImpl<ScheduleDetailMapper, ScheduleDetail>
        implements ScheduleDetailService {

    @Autowired
    private ScheduleDetailMapper scheduleDetailMapper;

    @Override
    public List<ScheduleDetail> getDetailsByScheduleId(Long scheduleId) {
        log.info("根据排产计划ID查询明细, scheduleId: {}", scheduleId);
        return scheduleDetailMapper.selectByScheduleId(scheduleId);
    }

    @Override
    public List<ScheduleDetail> getDetailsByOrderId(Long orderId) {
        log.info("根据工单ID查询排产明细, orderId: {}", orderId);
        return scheduleDetailMapper.selectByOrderId(orderId);
    }

    @Override
    public ScheduleDetail getDetailByTaskId(Long taskId) {
        log.info("根据任务ID查询排产明细, taskId: {}", taskId);
        return scheduleDetailMapper.selectByTaskId(taskId);
    }

    @Override
    @Transactional
    public boolean batchSaveDetails(List<ScheduleDetail> details) {
        log.info("批量保存排产明细, 数量: {}", details.size());

        try {
            if (details.isEmpty()) {
                return true;
            }

            int result = scheduleDetailMapper.batchInsert(details);
            boolean success = result == details.size();

            if (success) {
                log.info("批量保存排产明细成功, 数量: {}", details.size());
            } else {
                log.warn("批量保存排产明细部分失败, 期望: {}, 实际: {}", details.size(), result);
            }
            return success;

        } catch (Exception e) {
            log.error("批量保存排产明细失败", e);
            throw new RuntimeException("批量保存排产明细失败: " + e.getMessage());
        }
    }

    @Override
    public boolean checkResourceConflict(Long resourceId, LocalDateTime startTime, LocalDateTime endTime, Long excludeDetailId) {
        log.debug("检查资源时间冲突, resourceId: {}, startTime: {}, endTime: {}, excludeDetailId: {}",
                resourceId, startTime, endTime, excludeDetailId);

        int count = scheduleDetailMapper.countResourceConflict(resourceId, startTime, endTime, excludeDetailId);
        return count > 0;
    }

    @Override
    @Transactional
    public boolean updateDetailStatus(Long id, String status) {
        log.info("更新排产明细状态, ID: {}, 状态: {}", id, status);

        try {
            int result = scheduleDetailMapper.updateStatus(id, status);
            return result > 0;
        } catch (Exception e) {
            log.error("更新排产明细状态失败, ID: {}", id, e);
            throw new RuntimeException("更新排产明细状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateActualStartTime(Long id, LocalDateTime actualStartTime) {
        log.info("更新排产明细实际开始时间, ID: {}, actualStartTime: {}", id, actualStartTime);

        try {
            int result = scheduleDetailMapper.updateActualStartTime(id, actualStartTime);
            return result > 0;
        } catch (Exception e) {
            log.error("更新排产明细实际开始时间失败, ID: {}", id, e);
            throw new RuntimeException("更新排产明细实际开始时间失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean updateActualEndTime(Long id, LocalDateTime actualEndTime) {
        log.info("更新排产明细实际结束时间, ID: {}, actualEndTime: {}", id, actualEndTime);

        try {
            int result = scheduleDetailMapper.updateActualEndTime(id, actualEndTime);
            return result > 0;
        } catch (Exception e) {
            log.error("更新排产明细实际结束时间失败, ID: {}", id, e);
            throw new RuntimeException("更新排产明细实际结束时间失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean deleteByScheduleId(Long scheduleId) {
        log.info("根据排产计划ID删除明细, scheduleId: {}", scheduleId);

        try {
            int result = scheduleDetailMapper.deleteByScheduleId(scheduleId);
            log.info("删除排产明细成功, scheduleId: {}, 删除数量: {}", scheduleId, result);
            return true;
        } catch (Exception e) {
            log.error("根据排产计划ID删除明细失败, scheduleId: {}", scheduleId, e);
            throw new RuntimeException("删除排产明细失败: " + e.getMessage());
        }
    }

    @Override
    public List<ScheduleDetail> getResourceWorkload(Long resourceId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("获取资源的排产负载, resourceId: {}, startDate: {}, endDate: {}", resourceId, startDate, endDate);
        return scheduleDetailMapper.selectResourceWorkload(resourceId, startDate, endDate);
    }
}
package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ScheduleDetail;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleDetailService extends IService<ScheduleDetail> {

    /**
     * 根据排产计划ID查询明细
     */
    List<ScheduleDetail> getDetailsByScheduleId(Long scheduleId);

    /**
     * 根据工单ID查询排产明细
     */
    List<ScheduleDetail> getDetailsByOrderId(Long orderId);

    /**
     * 根据任务ID查询排产明细
     */
    ScheduleDetail getDetailByTaskId(Long taskId);

    /**
     * 批量保存排产明细
     */
    boolean batchSaveDetails(List<ScheduleDetail> details);

    /**
     * 检查资源时间冲突
     */
    boolean checkResourceConflict(Long resourceId, LocalDateTime startTime, LocalDateTime endTime, Long excludeDetailId);

    /**
     * 更新排产明细状态
     */
    boolean updateDetailStatus(Long id, String status);

    /**
     * 更新实际开始时间
     */
    boolean updateActualStartTime(Long id, LocalDateTime actualStartTime);

    /**
     * 更新实际结束时间
     */
    boolean updateActualEndTime(Long id, LocalDateTime actualEndTime);

    /**
     * 根据排产计划ID删除明细
     */
    boolean deleteByScheduleId(Long scheduleId);

    /**
     * 获取资源的排产负载
     */
    List<ScheduleDetail> getResourceWorkload(Long resourceId, LocalDateTime startDate, LocalDateTime endDate);
}
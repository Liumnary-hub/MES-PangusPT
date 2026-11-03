package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ScheduleDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleDetailMapper extends BaseMapper<ScheduleDetail> {

    /**
     * 根据排产计划ID查询明细
     */
    List<ScheduleDetail> selectByScheduleId(@Param("scheduleId") Long scheduleId);

    /**
     * 根据工单ID查询排产明细
     */
    List<ScheduleDetail> selectByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据任务ID查询排产明细
     */
    ScheduleDetail selectByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据资源ID和时间范围查询排产明细
     */
    List<ScheduleDetail> selectByResourceAndTime(@Param("resourceId") Long resourceId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    /**
     * 批量插入排产明细
     */
    int batchInsert(@Param("list") List<ScheduleDetail> scheduleDetails);

    /**
     * 更新排产明细状态
     */
    int updateStatus(@Param("id") Long id, @Param("status") String status);

    /**
     * 更新实际开始时间
     */
    int updateActualStartTime(@Param("id") Long id, @Param("actualStartTime") LocalDateTime actualStartTime);

    /**
     * 更新实际结束时间
     */
    int updateActualEndTime(@Param("id") Long id, @Param("actualEndTime") LocalDateTime actualEndTime);

    /**
     * 根据排产计划ID删除明细
     */
    int deleteByScheduleId(@Param("scheduleId") Long scheduleId);

    /**
     * 检查资源时间冲突
     */
    int countResourceConflict(@Param("resourceId") Long resourceId,
                              @Param("startTime") LocalDateTime startTime,
                              @Param("endTime") LocalDateTime endTime,
                              @Param("excludeDetailId") Long excludeDetailId);

    /**
     * 获取资源的排产负载
     */
    List<ScheduleDetail> selectResourceWorkload(@Param("resourceId") Long resourceId,
                                                @Param("startDate") LocalDateTime startDate,
                                                @Param("endDate") LocalDateTime endDate);
}
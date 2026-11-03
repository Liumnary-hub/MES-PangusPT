package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.WorkShift;

import java.util.List;

public interface WorkShiftService extends IService<WorkShift> {

    /**
     * 分页查询班次
     */
    IPage<WorkShift> getShiftPage(Page<WorkShift> page, String shiftCode, String shiftName, Boolean isActive);

    /**
     * 获取所有启用的班次
     */
    List<WorkShift> getActiveShifts();

    /**
     * 根据班次编码查询
     */
    WorkShift getShiftByCode(String shiftCode);

    /**
     * 检查班次编码是否重复
     */
    boolean isShiftCodeDuplicate(String shiftCode, Long excludeId);

    /**
     * 更新班次启用状态
     */
    boolean updateShiftStatus(Long id, Boolean isActive);

    /**
     * 获取默认班次
     */
    WorkShift getDefaultShift();
}
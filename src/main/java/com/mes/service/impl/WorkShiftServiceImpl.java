package com.mes.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.WorkShift;
import com.mes.mapper.WorkShiftMapper;
import com.mes.service.WorkShiftService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class WorkShiftServiceImpl extends ServiceImpl<WorkShiftMapper, WorkShift>
        implements WorkShiftService {

    @Autowired
    private WorkShiftMapper workShiftMapper;

    @Override
    public IPage<WorkShift> getShiftPage(Page<WorkShift> page, String shiftCode, String shiftName, Boolean isActive) {
        log.info("分页查询班次, shiftCode: {}, shiftName: {}, isActive: {}", shiftCode, shiftName, isActive);
        return workShiftMapper.selectShiftPage(page, shiftCode, shiftName, isActive);
    }

    @Override
    public List<WorkShift> getActiveShifts() {
        log.debug("获取所有启用的班次");
        return workShiftMapper.selectActiveShifts();
    }

    @Override
    public WorkShift getShiftByCode(String shiftCode) {
        log.debug("根据班次编码查询, shiftCode: {}", shiftCode);
        return workShiftMapper.selectByShiftCode(shiftCode);
    }

    @Override
    public boolean isShiftCodeDuplicate(String shiftCode, Long excludeId) {
        int count = workShiftMapper.countByShiftCode(shiftCode, excludeId);
        return count > 0;
    }

    @Override
    @Transactional
    public boolean updateShiftStatus(Long id, Boolean isActive) {
        log.info("更新班次启用状态, ID: {}, isActive: {}", id, isActive);

        try {
            int result = workShiftMapper.updateActiveStatus(id, isActive);
            boolean success = result > 0;

            if (success) {
                log.info("班次状态更新成功, ID: {}", id);
            }
            return success;

        } catch (Exception e) {
            log.error("更新班次状态失败, ID: {}", id, e);
            throw new RuntimeException("更新班次状态失败: " + e.getMessage());
        }
    }

    @Override
    public WorkShift getDefaultShift() {
        log.debug("获取默认班次");
        return workShiftMapper.selectDefaultShift();
    }

    @Override
    @Transactional
    public boolean save(WorkShift entity) {
        // 重写save方法，添加业务逻辑
        if (entity.getId() == null) {
            // 新增时检查编码重复
            if (isShiftCodeDuplicate(entity.getShiftCode(), null)) {
                throw new RuntimeException("班次编码已存在: " + entity.getShiftCode());
            }
            entity.setCreateTime(LocalDateTime.now());
        }
        entity.setUpdateTime(LocalDateTime.now());

        return super.save(entity);
    }

    @Override
    @Transactional
    public boolean updateById(WorkShift entity) {
        // 重写update方法，添加业务逻辑
        if (entity.getShiftCode() != null) {
            if (isShiftCodeDuplicate(entity.getShiftCode(), entity.getId())) {
                throw new RuntimeException("班次编码已存在: " + entity.getShiftCode());
            }
        }
        entity.setUpdateTime(LocalDateTime.now());

        return super.updateById(entity);
    }
}
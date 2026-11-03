package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.WarehouseLocationDTO;
import com.mes.dto.WarehouseLocationQueryDTO;
import com.mes.entity.Inventory;
import com.mes.entity.WarehouseLocation;
import com.mes.mapper.InventoryMapper;
import com.mes.mapper.WarehouseLocationMapper;
import com.mes.service.WarehouseLocationService;
import com.mes.util.SecurityUtils;
import com.mes.util.TableDataInfo;
import com.mes.vo.WarehouseLocationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseLocationServiceImpl extends ServiceImpl<WarehouseLocationMapper, WarehouseLocation> implements WarehouseLocationService {

    private final WarehouseLocationMapper locationMapper;
    private final InventoryMapper inventoryMapper;

    @Override
    public TableDataInfo<WarehouseLocationVO> selectLocationPage(WarehouseLocationQueryDTO queryDTO) {
        int pageNum = queryDTO.getPageNum() != null ? queryDTO.getPageNum() : 1;
        int pageSize = queryDTO.getPageSize() != null ? queryDTO.getPageSize() : 10;

        Page<WarehouseLocationVO> page = new Page<>(pageNum, pageSize);
        WarehouseLocationVO queryVO = new WarehouseLocationVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        IPage<WarehouseLocationVO> result = locationMapper.selectLocationPage(page, queryVO);

        // 计算使用率
        List<WarehouseLocationVO> voList = result.getRecords();
        for (WarehouseLocationVO vo : voList) {
            calculateUsageRate(vo);
        }

        TableDataInfo<WarehouseLocationVO> tableData = new TableDataInfo<>();
        tableData.setRows(voList);
        tableData.setTotal(result.getTotal());
        tableData.setCode(200);
        tableData.setMsg("查询成功");

        return tableData;
    }

    @Override
    public List<WarehouseLocationVO> selectLocationList(WarehouseLocationQueryDTO queryDTO) {
        WarehouseLocationVO queryVO = new WarehouseLocationVO();
        BeanUtils.copyProperties(queryDTO, queryVO);

        Page<WarehouseLocationVO> page = new Page<>(1, Integer.MAX_VALUE);
        IPage<WarehouseLocationVO> result = locationMapper.selectLocationPage(page, queryVO);

        List<WarehouseLocationVO> voList = result.getRecords();
        for (WarehouseLocationVO vo : voList) {
            calculateUsageRate(vo);
        }

        return voList;
    }

    @Override
    public List<WarehouseLocationVO> selectByWarehouseCode(String warehouseCode) {
        LambdaQueryWrapper<WarehouseLocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseLocation::getWarehouseCode, warehouseCode);
        wrapper.eq(WarehouseLocation::getStatus, "available");

        List<WarehouseLocation> locations = locationMapper.selectList(wrapper);
        return locations.stream()
                .map(this::convertToVO)
                .peek(this::calculateUsageRate)
                .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseLocationVO> selectByLocationType(String locationType) {
        LambdaQueryWrapper<WarehouseLocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseLocation::getLocationType, locationType);
        wrapper.eq(WarehouseLocation::getStatus, "available");

        List<WarehouseLocation> locations = locationMapper.selectList(wrapper);
        return locations.stream()
                .map(this::convertToVO)
                .peek(this::calculateUsageRate)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertLocation(WarehouseLocationDTO locationDTO) {
        // 检查库位编码唯一性
        if (!checkLocationCodeUnique(locationDTO.getLocationCode(), null)) {
            throw new RuntimeException("库位编码已存在: " + locationDTO.getLocationCode());
        }

        WarehouseLocation location = convertToEntity(locationDTO);
        location.setCreateUser(SecurityUtils.getUsername());
        location.setUpdateUser(SecurityUtils.getUsername());

        return locationMapper.insert(location) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLocation(WarehouseLocationDTO locationDTO) {
        // 检查库位编码唯一性
        if (!checkLocationCodeUnique(locationDTO.getLocationCode(), locationDTO.getId())) {
            throw new RuntimeException("库位编码已存在: " + locationDTO.getLocationCode());
        }

        WarehouseLocation location = convertToEntity(locationDTO);
        location.setUpdateUser(SecurityUtils.getUsername());
        location.setUpdateTime(LocalDateTime.now());

        return locationMapper.updateById(location) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLocationByIds(List<Long> ids) {
        for (Long id : ids) {
            // 检查库位是否被使用
            if (isLocationInUse(id)) {
                WarehouseLocation location = locationMapper.selectById(id);
                throw new RuntimeException("库位【" + location.getLocationName() + "】已被使用，无法删除");
            }
            locationMapper.deleteById(id);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateLocationUsage(Long locationId, Double usage) {
        WarehouseLocation location = locationMapper.selectById(locationId);
        if (location == null) {
            throw new RuntimeException("库位不存在");
        }

        location.setCurrentUsage(BigDecimal.valueOf(usage));
        location.setUpdateUser(SecurityUtils.getUsername());
        location.setUpdateTime(LocalDateTime.now());

        // 更新库位状态
        if (location.getCapacity() != null && usage >= location.getCapacity().doubleValue()) {
            location.setStatus("full");
        } else {
            location.setStatus("available");
        }

        return locationMapper.updateById(location) > 0;
    }

    @Override
    public boolean checkLocationCodeUnique(String locationCode, Long excludeId) {
        LambdaQueryWrapper<WarehouseLocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseLocation::getLocationCode, locationCode);
        if (excludeId != null) {
            wrapper.ne(WarehouseLocation::getId, excludeId);
        }
        int count = Math.toIntExact(locationMapper.selectCount(wrapper));
        return count == 0;
    }

    @Override
    public List<WarehouseLocationVO> getAvailableLocations(String locationType) {
        LambdaQueryWrapper<WarehouseLocation> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WarehouseLocation::getLocationType, locationType);
        wrapper.eq(WarehouseLocation::getStatus, "available");

        List<WarehouseLocation> locations = locationMapper.selectList(wrapper);
        return locations.stream()
                .map(this::convertToVO)
                .peek(this::calculateUsageRate)
                .filter(vo -> {
                    // 过滤掉已满的库位
                    if (vo.getCapacity() == null) return true;
                    return vo.getCurrentUsage().doubleValue() < vo.getCapacity().doubleValue();
                })
                .collect(Collectors.toList());
    }

    /**
     * 计算库位使用率
     */
    private void calculateUsageRate(WarehouseLocationVO vo) {
        if (vo.getCapacity() != null && vo.getCapacity().doubleValue() > 0) {
            double usageRate = (vo.getCurrentUsage().doubleValue() / vo.getCapacity().doubleValue()) * 100;
            vo.setUsageRate(BigDecimal.valueOf(usageRate));

            // 设置使用状态
            if (usageRate >= 100) {
                vo.setUsageStatus("已满");
            } else if (usageRate >= 80) {
                vo.setUsageStatus("拥挤");
            } else if (usageRate >= 50) {
                vo.setUsageStatus("正常");
            } else {
                vo.setUsageStatus("空闲");
            }
        } else {
            vo.setUsageRate(BigDecimal.ZERO);
            vo.setUsageStatus("空闲");
        }
    }

    /**
     * Entity 转 VO
     */
    private WarehouseLocationVO convertToVO(WarehouseLocation location) {
        WarehouseLocationVO vo = new WarehouseLocationVO();
        BeanUtils.copyProperties(location, vo);
        vo.setLocationTypeDesc(getLocationTypeDesc(location.getLocationType()));
        vo.setStatusDesc(getStatusDesc(location.getStatus()));
        return vo;
    }

    /**
     * DTO 转 Entity
     */
    private WarehouseLocation convertToEntity(WarehouseLocationDTO dto) {
        WarehouseLocation location = new WarehouseLocation();
        BeanUtils.copyProperties(dto, location);
        return location;
    }

    /**
     * 检查库位是否被使用
     */
    private boolean isLocationInUse(Long locationId) {
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getLocationId, locationId);
        int count = Math.toIntExact(inventoryMapper.selectCount(wrapper));
        return count > 0;
    }

    /**
     * 获取库位类型描述
     */
    private String getLocationTypeDesc(String locationType) {
        if (locationType == null) return "";
        switch (locationType) {
            case "RAW": return "原料";
            case "SEMI": return "半成品";
            case "PROD": return "成品";
            case "TOOL": return "工具";
            default: return locationType;
        }
    }

    /**
     * 获取状态描述
     */
    private String getStatusDesc(String status) {
        if (status == null) return "";
        switch (status) {
            case "available": return "可用";
            case "full": return "已满";
            case "maintenance": return "维护中";
            default: return status;
        }
    }
}
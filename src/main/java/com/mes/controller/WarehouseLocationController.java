package com.mes.controller;

import com.mes.dto.WarehouseLocationDTO;
import com.mes.dto.WarehouseLocationQueryDTO;
import com.mes.service.WarehouseLocationService;
import com.mes.util.TableDataInfo;
import com.mes.vo.WarehouseLocationVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库库位管理控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouse-location")
public class WarehouseLocationController {

    private final WarehouseLocationService locationService;

    /**
     * 分页查询库位列表
     */
    @GetMapping("/list")
    public TableDataInfo<WarehouseLocationVO> list(WarehouseLocationQueryDTO queryDTO) {
        return locationService.selectLocationPage(queryDTO);
    }

    /**
     * 查询库位列表
     */
    @GetMapping("/all")
    public List<WarehouseLocationVO> all(WarehouseLocationQueryDTO queryDTO) {
        return locationService.selectLocationList(queryDTO);
    }

    /**
     * 获取库位详细信息
     */
    @GetMapping("/{id}")
    public WarehouseLocationVO getInfo(@PathVariable Long id) {
        // 这里需要实现根据ID获取库位详情的方法
        // 暂时返回null，实际开发中需要实现
        return null;
    }

    /**
     * 根据仓库编码查询库位
     */
    @GetMapping("/warehouse/{warehouseCode}")
    public List<WarehouseLocationVO> getByWarehouseCode(@PathVariable String warehouseCode) {
        return locationService.selectByWarehouseCode(warehouseCode);
    }

    /**
     * 根据类型查询库位
     */
    @GetMapping("/type/{locationType}")
    public List<WarehouseLocationVO> getByLocationType(@PathVariable String locationType) {
        return locationService.selectByLocationType(locationType);
    }

    /**
     * 新增库位
     */
    @PostMapping
    public boolean add(@Validated @RequestBody WarehouseLocationDTO locationDTO) {
        return locationService.insertLocation(locationDTO);
    }

    /**
     * 修改库位
     */
    @PutMapping
    public boolean edit(@Validated @RequestBody WarehouseLocationDTO locationDTO) {
        return locationService.updateLocation(locationDTO);
    }

    /**
     * 删除库位
     */
    @DeleteMapping("/{ids}")
    public boolean remove(@PathVariable List<Long> ids) {
        return locationService.deleteLocationByIds(ids);
    }

    /**
     * 更新库位使用量
     */
    @PutMapping("/{id}/usage")
    public boolean updateUsage(@PathVariable Long id, @RequestParam Double usage) {
        return locationService.updateLocationUsage(id, usage);
    }

    /**
     * 获取可用库位
     */
    @GetMapping("/available")
    public List<WarehouseLocationVO> getAvailableLocations(@RequestParam String locationType) {
        return locationService.getAvailableLocations(locationType);
    }

    /**
     * 检查库位编码是否唯一
     */
    @GetMapping("/checkLocationCodeUnique")
    public boolean checkLocationCodeUnique(@RequestParam String locationCode,
                                           @RequestParam(required = false) Long excludeId) {
        return locationService.checkLocationCodeUnique(locationCode, excludeId);
    }
}
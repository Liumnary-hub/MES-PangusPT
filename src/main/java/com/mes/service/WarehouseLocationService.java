package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.WarehouseLocationDTO;
import com.mes.dto.WarehouseLocationQueryDTO;
import com.mes.entity.WarehouseLocation;
import com.mes.util.TableDataInfo;
import com.mes.vo.WarehouseLocationVO;

import java.util.List;

/**
 * 仓库库位服务接口
 */
public interface WarehouseLocationService extends IService<WarehouseLocation> {

    /**
     * 分页查询库位列表
     */
    TableDataInfo<WarehouseLocationVO> selectLocationPage(WarehouseLocationQueryDTO queryDTO);

    /**
     * 查询库位列表
     */
    List<WarehouseLocationVO> selectLocationList(WarehouseLocationQueryDTO queryDTO);

    /**
     * 根据仓库编码查询库位
     */
    List<WarehouseLocationVO> selectByWarehouseCode(String warehouseCode);

    /**
     * 根据库位类型查询库位
     */
    List<WarehouseLocationVO> selectByLocationType(String locationType);

    /**
     * 新增库位
     */
    boolean insertLocation(WarehouseLocationDTO locationDTO);

    /**
     * 修改库位
     */
    boolean updateLocation(WarehouseLocationDTO locationDTO);

    /**
     * 删除库位
     */
    boolean deleteLocationByIds(List<Long> ids);

    /**
     * 更新库位使用量
     */
    boolean updateLocationUsage(Long locationId, Double usage);

    /**
     * 检查库位编码是否唯一
     */
    boolean checkLocationCodeUnique(String locationCode, Long excludeId);

    /**
     * 获取可用库位
     */
    List<WarehouseLocationVO> getAvailableLocations(String locationType);
}
package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.WarehouseLocation;
import com.mes.vo.WarehouseLocationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 仓库库位 Mapper 接口
 */
@Mapper
public interface WarehouseLocationMapper extends BaseMapper<WarehouseLocation> {

    @Select("SELECT * FROM mes_warehouse_location WHERE location_code = #{locationCode} AND is_deleted = 0")
    WarehouseLocation selectByLocationCode(@Param("locationCode") String locationCode);

    @Select("SELECT * FROM mes_warehouse_location WHERE location_type = #{locationType} AND status = 'available' AND is_deleted = 0")
    List<WarehouseLocation> selectAvailableByType(@Param("locationType") String locationType);

    IPage<WarehouseLocationVO> selectLocationPage(Page<WarehouseLocationVO> page, WarehouseLocationVO queryVO);
    List<WarehouseLocation> selectAvailableLocations(@Param("locationType") String locationType);
}
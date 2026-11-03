package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.mes.entity.PickList;
import com.mes.vo.PickListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 领料单 Mapper 接口
 */
@Mapper
public interface PickListMapper extends BaseMapper<PickList> {

    /**
     * 分页查询领料单
     */
    IPage<PickListVO> selectPickListPage(Page<PickListVO> page, @Param("vo") PickListVO vo);

    /**
     * 根据工单查询领料单
     */
    @Select("SELECT * FROM mes_pick_list WHERE work_order_id = #{workOrderId} AND is_deleted = 0")
    List<PickList> selectByWorkOrderId(@Param("workOrderId") Long workOrderId);

    /**
     * 根据领料单号查询
     */
    @Select("SELECT * FROM mes_pick_list WHERE pick_list_code = #{pickListCode} AND is_deleted = 0")
    PickList selectByPickListCode(@Param("pickListCode") String pickListCode);
}
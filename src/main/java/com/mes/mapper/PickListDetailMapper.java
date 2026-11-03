package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.mes.entity.PickListDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 领料单明细 Mapper 接口
 */
@Mapper
public interface PickListDetailMapper extends BaseMapper<PickListDetail> {

    @Select("SELECT * FROM mes_pick_list_detail WHERE pick_list_id = #{pickListId}")
    List<PickListDetail> selectByPickListId(@Param("pickListId") Long pickListId);

    @Select("SELECT * FROM mes_pick_list_detail WHERE pick_list_id = #{pickListId} AND status = 'pending'")
    List<PickListDetail> selectPendingByPickListId(@Param("pickListId") Long pickListId);

    @Select("SELECT * FROM mes_pick_list_detail WHERE batch_number = #{batchNumber}")
    List<PickListDetail> selectByBatchNumber(@Param("batchNumber") String batchNumber);
}
package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.PickListCreateDTO;
import com.mes.dto.PickListExecuteDTO;

import com.mes.dto.PickListQueryDTO;
import com.mes.entity.PickList;
import com.mes.util.TableDataInfo;
import com.mes.vo.PickListVO;

import java.util.List;

/**
 * 领料单服务接口
 */
public interface PickListService extends IService<PickList> {

    /**
     * 分页查询领料单列表
     */
    TableDataInfo<PickListVO> selectPickListPage(PickListQueryDTO queryDTO);

    /**
     * 查询领料单列表
     */
    List<PickListVO> selectPickListList(PickListQueryDTO queryDTO);

    /**
     * 根据工单生成领料单
     */
    boolean createPickListFromWorkOrder(Long workOrderId);

    /**
     * 手动创建领料单
     */
    boolean createPickList(PickListCreateDTO createDTO);

    /**
     * 执行领料
     */
    boolean executePickList(PickListExecuteDTO executeDTO);

    /**
     * 取消领料单
     */
    boolean cancelPickList(Long pickListId, String reason);

    /**
     * 完成领料单
     */
    boolean completePickList(Long pickListId);

    /**
     * 获取领料单详情
     */
    PickListVO getPickListDetail(Long pickListId);

    /**
     * 打印领料单
     */
    boolean printPickList(Long pickListId);
}
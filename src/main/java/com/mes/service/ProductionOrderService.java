package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProductionOrder;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductionOrderService extends IService<ProductionOrder> {

    /**
     * 创建生产工单
     */
    boolean createProductionOrder(ProductionOrder order);

    /**
     * 下发工单
     */
    boolean releaseOrder(Long orderId);

    /**
     * 完成工单
     */
    boolean completeOrder(Long orderId);

    /**
     * 分页查询工单
     */
    Page<ProductionOrder> getOrderPage(Integer pageNum, Integer pageSize, String orderCode, String productCode, String status);

    /**
     * 根据状态查询工单
     */
    List<ProductionOrder> getOrdersByStatus(String status);

    /**
     * 排产工单
     */
    boolean scheduleOrder(Long orderId, LocalDateTime startTime, LocalDateTime endTime);

    IPage<ProductionOrder> getOrdersByCondition(Page<ProductionOrder> page, String orderCode, String productCode, String status);

    boolean updateOrderStatus(Long orderId, String status);


    /**
     * 完成计划 - 将工单从草稿改为已计划状态
     */
//    boolean planOrder(Long orderId);

    /**
     * 取消工单
     */

}
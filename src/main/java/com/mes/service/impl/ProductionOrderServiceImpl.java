package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ProductionOrder;
import com.mes.mapper.ProductionOrderMapper;
import com.mes.service.ProductionOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductionOrderServiceImpl extends ServiceImpl<ProductionOrderMapper, ProductionOrder>
        implements ProductionOrderService {

    @Override
    @Transactional
    public boolean createProductionOrder(ProductionOrder order) {
        // 生成工单编号
        String orderCode = "WO" + System.currentTimeMillis();
        order.setOrderCode(orderCode);
        order.setStatus("draft");
        order.setCompletedQuantity(0);

        return save(order);
    }

    @Override
    @Transactional
    public boolean releaseOrder(Long orderId) {
        ProductionOrder order = getById(orderId);
        if (order != null && "planned".equals(order.getStatus())) {
            order.setStatus("released");
            order.setActualStartTime(LocalDateTime.now());
            return updateById(order);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean completeOrder(Long orderId) {
        ProductionOrder order = getById(orderId);
        if (order != null && "in_progress".equals(order.getStatus())) {
            order.setStatus("completed");
            order.setActualEndTime(LocalDateTime.now());
            return updateById(order);
        }
        return false;
    }

    @Override
    public Page<ProductionOrder> getOrderPage(Integer pageNum, Integer pageSize, String orderCode, String productCode, String status) {
        Page<ProductionOrder> page = new Page<>(pageNum, pageSize);
        return baseMapper.selectOrderPage(page, orderCode, productCode, status, null, null);
    }

    @Override
    public List<ProductionOrder> getOrdersByStatus(String status) {
        return baseMapper.selectByStatus(status);
    }

    @Override
    @Transactional
    public boolean scheduleOrder(Long orderId, LocalDateTime startTime, LocalDateTime endTime) {
        ProductionOrder order = getById(orderId);
        if (order != null && "draft".equals(order.getStatus())) {
            order.setPlannedStartTime(startTime);
            order.setPlannedEndTime(endTime);
            order.setStatus("planned");
            return updateById(order);
        }
        return false;
    }

    @Override
    public IPage<ProductionOrder> getOrdersByCondition(Page<ProductionOrder> page, String orderCode, String productCode, String status) {
        LambdaQueryWrapper<ProductionOrder> queryWrapper = new LambdaQueryWrapper<>();

        if (orderCode != null && !orderCode.trim().isEmpty()) {
            queryWrapper.like(ProductionOrder::getOrderCode, orderCode);
        }
        if (productCode != null && !productCode.trim().isEmpty()) {
            queryWrapper.like(ProductionOrder::getProductCode, productCode);
        }
        if (status != null && !status.trim().isEmpty()) {
            queryWrapper.eq(ProductionOrder::getStatus, status);
        }

        queryWrapper.orderByDesc(ProductionOrder::getCreateTime);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean updateOrderStatus(Long orderId, String status) {
        ProductionOrder order = getById(orderId);
        if (order != null) {
            order.setStatus(status);

            // 根据状态更新相关时间
            if ("in_progress".equals(status) && order.getActualStartTime() == null) {
                order.setActualStartTime(LocalDateTime.now());
            } else if ("completed".equals(status) && order.getActualEndTime() == null) {
                order.setActualEndTime(LocalDateTime.now());
            }

            return updateById(order);
        }
        return false;
    }


}
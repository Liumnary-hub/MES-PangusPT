package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.dto.ProductionOrderDetail;
import com.mes.entity.ProductionOrder;
import com.mes.service.ProductionCoordinatorService;
import com.mes.service.ProductionOrderService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/orders")
public class ProductionOrderController {

    @Autowired
    private ProductionOrderService orderService;
    @Autowired
    private ProductionCoordinatorService coordinatorService;

    @GetMapping
    public Result<IPage<ProductionOrder>> getOrders(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderCode,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String status) {
        Page<ProductionOrder> page = new Page<>(pageNum, pageSize);
        IPage<ProductionOrder> result = orderService.getOrdersByCondition(page, orderCode, productCode, status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ProductionOrder> getOrderById(@PathVariable Long id) {
        ProductionOrder order = orderService.getById(id);
        if (order == null) {
            return Result.error("工单不存在");
        }
        return Result.success(order);
    }

    //获取工单详情
    @GetMapping("/{id}/detail")
    public Result<ProductionOrderDetail> getOrderDetailById(@PathVariable Long id) {
        try {
            ProductionOrderDetail detail = coordinatorService.getOrderDetail(id);
            if (detail == null) {
                System.out.println("工单不存在");
            }
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}/progress")
    public Result<Double>getOrderProgress(@PathVariable Long id) {
        try {
            Double progress = coordinatorService.getOrderProgress(id);
            return Result.success(progress);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }



    @PostMapping
    public Result<Boolean> createOrder(@RequestBody ProductionOrder order) {
        try {
            boolean success = orderService.createProductionOrder(order);
            return success ? Result.success(true, "创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateOrder(@PathVariable Long id, @RequestBody ProductionOrder order) {
        try {
            // 获取原有工单，检查状态
            ProductionOrder existingOrder = orderService.getById(id);
            if (existingOrder == null) {
                return Result.error("工单不存在");
            }

            // 只有草稿和已计划状态的工单可以编辑
            if (!"draft".equals(existingOrder.getStatus()) && !"planned".equals(existingOrder.getStatus())) {
                return Result.error("只有草稿和已计划状态的工单可以编辑");
            }

            order.setId(id);
            boolean success = orderService.updateById(order);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteOrder(@PathVariable Long id) {
        try {
            //获取工单状态
            ProductionOrder order = orderService.getById(id);
            if (order == null) {
                System.out.println("工单不存在");
            }
            if(!"draft".equals(order.getStatus())) {
                return Result.error("只有草稿状态的工单才能删除");
            }


            boolean success = orderService.removeById(id);
            return success ? Result.success(true, "删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean success = orderService.updateOrderStatus(id, status);
            return success ? Result.success(true, "状态更新成功") : Result.error("状态更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // ✅ 修复：完成工单计划
    @PutMapping("/{id}/plan")
    public Result<Boolean> planOrder(@PathVariable Long id) {
        try {
            boolean success = coordinatorService.planOrder(id);
            return success ? Result.success(true, "工单计划完成成功") : Result.error("工单计划完成失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    //取消工单
    @PutMapping("/{id}/cancel")
    public Result<Boolean> cancelOrder(@PathVariable Long id) {
        try {
            boolean success = coordinatorService.cancelOrder(id);
            return success ? Result.success(true,"工单取消完成") :Result.error("工单取消失败");
        }catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }




    @PutMapping("/{id}/release")
    public Result<Boolean> releaseOrder(@PathVariable Long id) {
        try {
            boolean success = coordinatorService.releaseOrderWithTasks(id);
            return success ? Result.success(true, "工单下发成功") : Result.error("工单下发失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    //这个接口已经移除
//    @PutMapping("/{id}/complete")
//    public Result<Boolean> completeOrder(@PathVariable Long id) {
//        try {
//            boolean success = orderService.completeOrder(id);
//            return success ? Result.success(true, "工单完成成功") : Result.error("工单完成失败");
//        } catch (Exception e) {
//            return Result.error(e.getMessage());
//        }
//    }

    @GetMapping("/status/{status}")
    public Result<List<ProductionOrder>> getOrdersByStatus(@PathVariable String status) {
        List<ProductionOrder> orders = orderService.lambdaQuery()
                .eq(ProductionOrder::getStatus, status)
                .list();
        return Result.success(orders);
    }

    @GetMapping("/all")
    public Result<List<ProductionOrder>> getAllOrders() {
        List<ProductionOrder> orders = orderService.list();
        return Result.success(orders);
    }

}
package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionTask;
import com.mes.service.ProductionCoordinatorService;
import com.mes.service.ProductionTaskService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production/tasks")
public class ProductionTaskController {

    @Autowired
    private ProductionTaskService taskService;

    @Autowired
    private ProductionCoordinatorService coordinatorService;

    @GetMapping
    public Result<IPage<ProductionTask>> getTasks(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String taskCode,
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long resourceId,
            @RequestParam(required = false) String status) {
        Page<ProductionTask> page = new Page<>(pageNum, pageSize);
        IPage<ProductionTask> result = taskService.getTasksByCondition(page, taskCode, orderId, resourceId, status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ProductionTask> getTaskById(@PathVariable Long id) {
        ProductionTask task = taskService.getById(id);
        if (task == null) {
            return Result.error("任务不存在");
        }
        return Result.success(task);
    }

    @PostMapping
    public Result<Boolean> createTask(@RequestBody ProductionTask task) {
        try {
            // 设置默认值
            if (task.getStatus() == null) {
                task.setStatus("pending");
            }
            if (task.getCompletedQuantity() == null) {
                task.setCompletedQuantity(0);
            }
            if (task.getQualifiedQuantity() == null) {
                task.setQualifiedQuantity(0);
            }
            if (task.getQualityStatus() == null) {
                task.setQualityStatus("pending");
            }

            boolean success = taskService.save(task);
            return success ? Result.success(true, "创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/from-order/{orderId}")
    public Result<Boolean> createTasksFromOrder(@PathVariable Long orderId) {
        try {
            boolean success = taskService.createTasksFromOrder(orderId);
            return success ? Result.success(true, "任务创建成功") : Result.error("任务创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateTask(@PathVariable Long id, @RequestBody ProductionTask task) {
        try {
            // 获取原有任务，检查状态
            ProductionTask existingTask = taskService.getById(id);
            if (existingTask == null) {
                return Result.error("任务不存在");
            }

            // 只有待执行状态的任务可以编辑基本信息
            if (!"pending".equals(existingTask.getStatus())) {
                return Result.error("只有待执行状态的任务可以编辑基本信息");
            }

            task.setId(id);
            boolean success = taskService.updateById(task);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTask(@PathVariable Long id) {
        try {
            // 获取任务，检查状态
            ProductionTask task = taskService.getById(id);
            if (task == null) {
                return Result.error("任务不存在");
            }

            // 只有待执行状态的任务可以删除
            if (!"pending".equals(task.getStatus())) {
                return Result.error("只有待执行状态的任务可以删除");
            }

            boolean success = taskService.removeById(id);
            return success ? Result.success(true, "删除成功") : Result.error("删除失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/start")
    public Result<Boolean> startTask(@PathVariable Long id, @RequestParam String operatorId) {
        try {
            // 这里可以添加操作员姓名的获取逻辑
            // 简化处理：使用operatorId作为operatorName
            String operatorName = operatorId;

            boolean success = coordinatorService.startTaskWithOrderSync(id, operatorId, operatorName);
            return success ? Result.success(true, "任务开始成功") : Result.error("任务开始失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/complete")
    public Result<Boolean> completeTask(@PathVariable Long id,
                                        @RequestParam Integer completedQuantity,
                                        @RequestParam Integer qualifiedQuantity) {
        try {
            boolean success = coordinatorService.completeTaskWithOrderSync(id, completedQuantity, qualifiedQuantity);
            return success ? Result.success(true, "任务完成成功") : Result.error("任务完成失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/progress")
    public Result<Boolean> updateProgress(@PathVariable Long id,
                                          @RequestParam Integer completedQuantity,
                                          @RequestParam Integer qualifiedQuantity,
                                          @RequestParam(required = false) Double actualTime) {
        try {
            // 如果未提供actualTime，设置为0
            if (actualTime == null) {
                actualTime = 0.0;
            }

            boolean success = coordinatorService.updateTaskProgressWithOrderSync(id, completedQuantity, qualifiedQuantity, actualTime);
            return success ? Result.success(true, "进度更新成功") : Result.error("进度更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    @PutMapping("/{id}/pause")
    public Result<Boolean> pauseTask(@PathVariable Long id)
    {
        try {
            boolean success = coordinatorService.pauseTask(id);
            return success ? Result.success(true,"任务暂停成功") : Result.error("任务暂停失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }

    }


    @PutMapping("/{id}/resume")
    public Result<Boolean> resumeTask(@PathVariable Long id)
    {
        try {
            boolean success = coordinatorService.resumeTask(id);
            return success ? Result.success(true,"任务恢复成功") : Result.error("任务恢复失败");
        } catch (Exception e) {
           return Result.error(e.getMessage());
        }

    }

    @GetMapping("/order/{orderId}")
    public Result<List<ProductionTask>> getTasksByOrder(@PathVariable Long orderId) {
        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getOrderId, orderId)
                .orderByAsc(ProductionTask::getOperationSequence)
                .list();
        return Result.success(tasks);
    }

    @GetMapping("/resource/{resourceId}")
    public Result<List<ProductionTask>> getTasksByResource(@PathVariable Long resourceId) {
        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getResourceId, resourceId)
                .orderByAsc(ProductionTask::getPlannedStartTime)
                .list();
        return Result.success(tasks);
    }

    @GetMapping("/resource/{resourceId}/pending")
    public Result<List<ProductionTask>> getPendingTasks(@PathVariable Long resourceId) {
        List<ProductionTask> tasks = taskService.lambdaQuery()
                .eq(ProductionTask::getResourceId, resourceId)
                .eq(ProductionTask::getStatus, "pending")
                .orderByAsc(ProductionTask::getPlannedStartTime)
                .list();
        return Result.success(tasks);
    }

    @GetMapping("/all")
    public Result<List<ProductionTask>> getAllTasks() {
        List<ProductionTask> tasks = taskService.list();
        return Result.success(tasks);
    }


}
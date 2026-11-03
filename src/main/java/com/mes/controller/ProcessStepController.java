package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProcessStep;
import com.mes.service.ProcessStepService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process/steps")
public class ProcessStepController {

    @Autowired
    private ProcessStepService stepService;

    @GetMapping
    public Result<IPage<ProcessStep>> getSteps(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String stepCode,
            @RequestParam(required = false) String stepName,
            @RequestParam(required = false) Long routeId) {
        Page<ProcessStep> page = new Page<>(pageNum, pageSize);
        IPage<ProcessStep> result = stepService.getStepsByCondition(page, stepCode, stepName, routeId);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ProcessStep> getStepById(@PathVariable Long id) {
        ProcessStep step = stepService.getById(id);
        if (step == null) {
            return Result.error("工序不存在");
        }
        return Result.success(step);
    }

    @PostMapping
    public Result<Boolean> createStep(@RequestBody ProcessStep step) {
        try {
            boolean success = stepService.save(step);
            return success ? Result.success(true, "创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateStep(@PathVariable Long id, @RequestBody ProcessStep step) {
        try {
            step.setId(id);
            boolean success = stepService.updateById(step);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteStep(@PathVariable Long id) {
        boolean success = stepService.removeById(id);
        return success ? Result.success(true, "删除成功") : Result.error("删除失败");
    }

    @GetMapping("/route/{routeId}")
    public Result<List<ProcessStep>> getStepsByRoute(@PathVariable Long routeId) {
        List<ProcessStep> steps = stepService.lambdaQuery()
                .eq(ProcessStep::getRouteId, routeId)
                .orderByAsc(ProcessStep::getSequence)
                .list();
        return Result.success(steps);
    }

    @PutMapping("/route/{routeId}/sequence")
    public Result<Boolean> updateStepSequence(@PathVariable Long routeId, @RequestBody List<Long> stepIds) {
        try {
            boolean success = stepService.updateStepSequence(routeId, stepIds);
            return success ? Result.success(true, "工序顺序更新成功") : Result.error("工序顺序更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/all")
    public Result<List<ProcessStep>> getAllSteps() {
        List<ProcessStep> steps = stepService.list();
        return Result.success(steps);
    }
}
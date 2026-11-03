package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProcessRoute;
import com.mes.service.ProcessRouteService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/process/routes")
public class ProcessRouteController {

    @Autowired
    private ProcessRouteService routeService;

    @GetMapping
    public Result<IPage<ProcessRoute>> getRoutes(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String routeCode,
            @RequestParam(required = false) String routeName,
            @RequestParam(required = false) String status) {
        Page<ProcessRoute> page = new Page<>(pageNum, pageSize);
        IPage<ProcessRoute> result = routeService.getRoutesByCondition(page, routeCode, routeName, status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ProcessRoute> getRouteById(@PathVariable Long id) {
        ProcessRoute route = routeService.getById(id);
        if (route == null) {
            return Result.error("工艺路线不存在");
        }
        return Result.success(route);
    }

    @PostMapping
    public Result<Boolean> createRoute(@RequestBody ProcessRoute route) {
        try {
            boolean success = routeService.save(route);
            return success ? Result.success(true, "创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateRoute(@PathVariable Long id, @RequestBody ProcessRoute route) {
        try {
            route.setId(id);
            boolean success = routeService.updateById(route);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteRoute(@PathVariable Long id) {
        boolean success = routeService.removeById(id);
        return success ? Result.success(true, "删除成功") : Result.error("删除失败");
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> toggleStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean success = routeService.toggleStatus(id, status);
            return success ? Result.success(true, "状态更新成功") : Result.error("状态更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/product/{productId}")
    public Result<List<ProcessRoute>> getRoutesByProduct(@PathVariable Long productId) {
        List<ProcessRoute> routes = routeService.lambdaQuery()
                .eq(ProcessRoute::getProductId, productId)
                .list();
        return Result.success(routes);
    }

    @GetMapping("/all")
    public Result<List<ProcessRoute>> getAllRoutes() {
        List<ProcessRoute> routes = routeService.list();
        return Result.success(routes);
    }

    @PostMapping("/{id}/clone")
    public Result<Boolean> cloneRoute(@PathVariable Long id, @RequestParam String newRouteCode) {
        try {
            boolean success = routeService.cloneRoute(id, newRouteCode);
            return success ? Result.success(true, "复制成功") : Result.error("复制失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
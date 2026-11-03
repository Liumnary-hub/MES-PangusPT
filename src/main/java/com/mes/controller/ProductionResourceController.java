package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductionResource;
import com.mes.service.ProductionResourceService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static javax.management.Query.eq;

@RestController
@RequestMapping("/api/production/resources")
public class ProductionResourceController {
    @Autowired
    private ProductionResourceService resourceService;


    @GetMapping
    public Result<IPage<ProductionResource>> getResources(
     @RequestParam(defaultValue = "1")Integer pageNum,
     @RequestParam(defaultValue = "10")Integer pageSize,
     @RequestParam(required = false)String resourceCode,
     @RequestParam(required = false)String resourceName,
     @RequestParam(required = false)String department,
     @RequestParam(required = false)String resourceType,
     @RequestParam(required = false)String status
    )
    {
        Page<ProductionResource> page = new Page<>(pageNum,pageSize);
        IPage<ProductionResource> result =resourceService.getResourcesByCondition(page,resourceCode,resourceName,department,resourceType,status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ProductionResource> getResourceById(@PathVariable Long id)
    {
        ProductionResource resource =resourceService.getById(id);
        if (resource == null) {
            return Result.error("资源不存在");
        }
        return Result.success(resource);
    }

    @PostMapping
    public Result<Boolean> createResourece(@RequestBody ProductionResource resource)
    {
        try {
            boolean success =resourceService.save(resource);
            return success ?Result.success(true,"创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateResource(@PathVariable Long id,@RequestBody ProductionResource resource)
    {
        try {
            boolean success =resourceService.updateById(resource);
            return success ? Result.success(true,"更新成功"):Result.error("更新失败");
        } catch (Exception e) {
           return Result.error(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteResource(@PathVariable Long id)
    {

            boolean success =resourceService.removeById(id);
            return success? Result.success(true,"删除成功"):Result.error("删除失败");
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> toggleStatus(@PathVariable Long id ,@RequestParam String status)
    {
        try {
            boolean success =resourceService.toggleStatus(id,status);
            return success? Result.success(true,"状态更新成功"):Result.error("状态更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
    @GetMapping("/all")
    public Result<List<ProductionResource>> getAllResources()
    {
        List<ProductionResource> resources = resourceService.lambdaQuery()
                .eq(ProductionResource::getStatus, "active")
                .list();
        return  Result.success(resources);
    }


}

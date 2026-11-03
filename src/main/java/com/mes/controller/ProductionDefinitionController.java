package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProductDefinition;
import com.mes.service.ProductDefinitionService;
import com.mes.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quality/products")
public class ProductionDefinitionController {
    @Autowired
    private ProductDefinitionService productService;

    @GetMapping
    public Result<IPage<ProductDefinition>> getProducts(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) String status) {

        Page<ProductDefinition> page = new Page<>(pageNum, pageSize);
        IPage<ProductDefinition> result = productService.getProductsByCondition(
                page, productCode, productName, productType, status);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    public Result<ProductDefinition> getProductById(@PathVariable Long id) {
        ProductDefinition product = productService.getById(id);
        if (product == null) {
            return Result.error("产品不存在");
        }
        return Result.success(product);
    }

    @PostMapping
    public Result<Boolean> createProduct(@RequestBody ProductDefinition product) {
        try {
            boolean success = productService.saveProduct(product);
            return success ? Result.success(true, "创建成功") : Result.error("创建失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Result<Boolean> updateProduct(@PathVariable Long id, @RequestBody ProductDefinition product) {
        product.setId(id);
        try {
            boolean success = productService.updateProduct(product);
            return success ? Result.success(true, "更新成功") : Result.error("更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProduct(@PathVariable Long id) {
        boolean success = productService.removeById(id);
        return success ? Result.success(true, "删除成功") : Result.error("删除失败");
    }

    @PutMapping("/{id}/status")
    public Result<Boolean> toggleStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean success = productService.toggleStatus(id, status);
            return success ? Result.success(true, "状态更新成功") : Result.error("状态更新失败");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/all")
    public Result<List<ProductDefinition>> getAllActiveProducts() {
        List<ProductDefinition> products = productService.lambdaQuery()
                .eq(ProductDefinition::getStatus, "active")
                .list();
        return Result.success(products);
    }


}

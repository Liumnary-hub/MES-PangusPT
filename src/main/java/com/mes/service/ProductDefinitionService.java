package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProductDefinition;

public interface  ProductDefinitionService extends IService<ProductDefinition> {
    IPage<ProductDefinition> getProductsByCondition(Page<ProductDefinition> page,
                                                    String productCode,
                                                    String productName,
                                                    String productType,
                                                    String status);

    boolean saveProduct(ProductDefinition product);

    boolean updateProduct(ProductDefinition product);

    boolean toggleStatus(Long id, String status);

}

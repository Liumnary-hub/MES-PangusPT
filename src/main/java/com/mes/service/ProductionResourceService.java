package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProductionResource;

import java.util.List;

public interface ProductionResourceService extends IService<ProductionResource> {
    IPage<ProductionResource> getResourcesByCondition(Page<ProductionResource> page,
                                                      String resourceCode,
                                                      String resourceName,
                                                      String resourceType,
                                                      String department,
                                                      String status);

    boolean saveResource(ProductionResource resource);

    boolean updateResource(ProductionResource resource);

    boolean toggleStatus(Long id, String status);

    List<ProductionResource> getResourcesByDepartment(String department);
}


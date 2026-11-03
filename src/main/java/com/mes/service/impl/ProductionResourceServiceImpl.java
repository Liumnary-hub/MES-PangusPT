package com.mes.service.impl;// src/main/java/com/pangu/mes/service/impl/ProductionResourceServiceImpl.java


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ProductionResource;
import com.mes.mapper.ProductionResourceMapper;
import com.mes.service.ProductionResourceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProductionResourceServiceImpl extends ServiceImpl<ProductionResourceMapper, ProductionResource>
        implements ProductionResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ProductionResourceServiceImpl.class);

    @Autowired
    private ProductionResourceMapper resourceMapper;

    @Override
    public IPage<ProductionResource> getResourcesByCondition(Page<ProductionResource> page,
                                                             String resourceCode,
                                                             String resourceName,
                                                             String resourceType,
                                                             String department,
                                                             String status) {
        return resourceMapper.selectByCondition(page, resourceCode, resourceName, resourceType, department, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveResource(ProductionResource resource) {
        try {
            logger.info("开始保存生产资源: {}", resource.getResourceCode());

            // 检查资源编码是否重复
            int count = resourceMapper.countByResourceCode(resource.getResourceCode());
            if (count > 0) {
                throw new RuntimeException("资源编码已存在: " + resource.getResourceCode());
            }

            // 设置默认值
            if (resource.getStatus() == null) {
                resource.setStatus("active");
            }
            resource.setCreateTime(LocalDateTime.now());
            resource.setUpdateTime(LocalDateTime.now());
            resource.setIsDeleted(0);

            // 保存到数据库
            boolean result = this.save(resource);

            if (result) {
                logger.info("生产资源保存成功: ID={}, Code={}", resource.getId(), resource.getResourceCode());
            } else {
                logger.error("生产资源保存失败，但未抛出异常");
            }

            return result;

        } catch (Exception e) {
            logger.error("保存生产资源失败: {}", e.getMessage(), e);
            throw new RuntimeException("保存失败: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateResource(ProductionResource resource) {
        // 检查资源编码是否重复（排除自身）
        ProductionResource existing = this.getById(resource.getId());
        if (existing != null && !existing.getResourceCode().equals(resource.getResourceCode())) {
            int count = resourceMapper.countByResourceCode(resource.getResourceCode());
            if (count > 0) {
                throw new RuntimeException("资源编码已存在");
            }
        }
        return this.updateById(resource);
    }

    @Override
    public boolean toggleStatus(Long id, String status) {
        ProductionResource resource = this.getById(id);
        if (resource == null) {
            throw new RuntimeException("资源不存在");
        }
        resource.setStatus(status);
        return this.updateById(resource);
    }

    @Override
    public List<ProductionResource> getResourcesByDepartment(String department) {
        LambdaQueryWrapper<ProductionResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductionResource::getDepartment, department)
                .eq(ProductionResource::getStatus, "active")
                .orderByAsc(ProductionResource::getResourceCode);
        return baseMapper.selectList(queryWrapper);
    }
}
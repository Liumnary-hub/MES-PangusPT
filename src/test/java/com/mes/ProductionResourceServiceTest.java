package com.mes;

import com.mes.entity.ProductionResource;
import com.mes.service.ProductionResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductionResourceServiceTest {

    @Autowired
    private ProductionResourceService resourceService;

    @Test
    void testCreateResource() {
        ProductionResource resource = new ProductionResource();
        resource.setResourceCode("TEST001");
        resource.setResourceName("测试设备");
        resource.setResourceType("equipment");
        resource.setDepartment("测试部门");
        resource.setStatus("active");
        resource.setSpecification("测试规格");

        boolean result = resourceService.save(resource);
        assertTrue(result);
        assertNotNull(resource.getId());
    }

    @Test
    void testGetResourcesByDepartment() {
        // 先创建测试数据
        ProductionResource resource = new ProductionResource();
        resource.setResourceCode("DEPT001");
        resource.setResourceName("部门测试设备");
        resource.setResourceType("equipment");
        resource.setDepartment("生产一部");
        resource.setStatus("active");
        resourceService.save(resource);

        // 测试查询
        List<ProductionResource> resources = resourceService.getResourcesByDepartment("生产一部");
        assertFalse(resources.isEmpty());
        assertEquals("生产一部", resources.get(0).getDepartment());
    }

//    @Test
//    void testUpdateResourceStatus() {
//        ProductionResource resource = new ProductionResource();
//        resource.setResourceCode("STATUS001");
//        resource.setResourceName("状态测试设备");
//        resource.setResourceType("equipment");
//        resource.setDepartment("生产一部");
//        resource.setStatus("active");
//        resourceService.save(resource);
//
//        // 测试状态更新
//        boolean result = resourceService.changeResourceStatus(resource.getId(), "inactive");
//        assertTrue(result);
//
//        ProductionResource updated = resourceService.getById(resource.getId());
//        assertEquals("inactive", updated.getStatus());
//    }
//
//    @Test
//    void testGetResourcesByType() {
//        ProductionResource resource = new ProductionResource();
//        resource.setResourceCode("TYPE001");
//        resource.setResourceName("类型测试设备");
//        resource.setResourceType("tool");
//        resource.setDepartment("生产一部");
//        resource.setStatus("active");
//        resourceService.save(resource);
//
//        List<ProductionResource> tools = resourceService.getResourcesByType("tool");
//        assertFalse(tools.isEmpty());
//        assertEquals("tool", tools.get(0).getResourceType());
//    }
}
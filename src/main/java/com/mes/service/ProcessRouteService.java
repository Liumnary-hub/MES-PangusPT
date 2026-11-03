package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProcessRoute;
import java.util.List;

public interface ProcessRouteService extends IService<ProcessRoute> {

    /**
     * 条件分页查询工艺路线
     */
    IPage<ProcessRoute> getRoutesByCondition(Page<ProcessRoute> page, String routeCode, String routeName, String status);

    /**
     * 更新状态
     */
    boolean toggleStatus(Long id, String status);

    /**
     * 根据产品ID查询工艺路线
     */
    List<ProcessRoute> getRoutesByProductId(Long productId);

    /**
     * 复制工艺路线
     */
    boolean cloneRoute(Long id, String newRouteCode);

    /**
     * 获取激活的工艺路线
     */
    List<ProcessRoute> getActiveRoutes();
}
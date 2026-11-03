package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ProcessRoute;
import com.mes.mapper.ProcessRouteMapper;
import com.mes.service.ProcessRouteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProcessRouteServiceImpl extends ServiceImpl<ProcessRouteMapper, ProcessRoute>
        implements ProcessRouteService {

    @Override
    public IPage<ProcessRoute> getRoutesByCondition(Page<ProcessRoute> page, String routeCode, String routeName, String status) {
        LambdaQueryWrapper<ProcessRoute> queryWrapper = new LambdaQueryWrapper<>();

        if (routeCode != null && !routeCode.trim().isEmpty()) {
            queryWrapper.like(ProcessRoute::getRouteCode, routeCode);
        }
        if (routeName != null && !routeName.trim().isEmpty()) {
            queryWrapper.like(ProcessRoute::getRouteName, routeName);
        }
        if (status != null && !status.trim().isEmpty()) {
            queryWrapper.eq(ProcessRoute::getStatus, status);
        }

        queryWrapper.orderByDesc(ProcessRoute::getCreateTime);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public boolean toggleStatus(Long id, String status) {
        ProcessRoute route = getById(id);
        if (route != null) {
            route.setStatus(status);
            return updateById(route);
        }
        return false;
    }

    @Override
    public List<ProcessRoute> getRoutesByProductId(Long productId) {
        LambdaQueryWrapper<ProcessRoute> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessRoute::getProductId, productId)
                .eq(ProcessRoute::getStatus, "active")
                .orderByDesc(ProcessRoute::getCreateTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public boolean cloneRoute(Long id, String newRouteCode) {
        ProcessRoute originalRoute = getById(id);
        if (originalRoute == null) {
            return false;
        }

        // 创建新路线
        ProcessRoute newRoute = new ProcessRoute();
        newRoute.setRouteCode(newRouteCode);
        newRoute.setRouteName(originalRoute.getRouteName() + "_副本");
        newRoute.setProductId(originalRoute.getProductId());
        newRoute.setVersion("1.0");
        newRoute.setStatus("active");
        newRoute.setDescription(originalRoute.getDescription());

        return save(newRoute);
    }

    @Override
    public List<ProcessRoute> getActiveRoutes() {
        LambdaQueryWrapper<ProcessRoute> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProcessRoute::getStatus, "active")
                .orderByAsc(ProcessRoute::getRouteName);
        return baseMapper.selectList(queryWrapper);
    }
}
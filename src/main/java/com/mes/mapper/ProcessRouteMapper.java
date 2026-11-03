package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.ProcessRoute;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProcessRouteMapper extends BaseMapper<ProcessRoute> {
    // 可以添加自定义的复杂查询方法
}
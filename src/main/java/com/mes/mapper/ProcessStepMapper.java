package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.ProcessStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProcessStepMapper extends BaseMapper<ProcessStep> {

    /**
     * 条件分页查询工序
     */
    Page<ProcessStep> selectStepsByCondition(Page<ProcessStep> page,
                                             @Param("stepCode") String stepCode,
                                             @Param("stepName") String stepName,
                                             @Param("routeId") Long routeId);

    /**
     * 根据工艺路线ID查询工序
     */
    List<ProcessStep> selectStepsByRouteId(@Param("routeId") Long routeId);

    /**
     * 根据资源类型查询工序
     */
    List<ProcessStep> selectStepsByResourceType(@Param("resourceType") String resourceType);

    /**
     * 删除指定工艺路线的所有工序
     */
    int deleteByRouteId(@Param("routeId") Long routeId);

    /**
     * 批量插入工序
     */
    int batchInsert(@Param("list") List<ProcessStep> stepList);

    /**
     * 更新工序顺序
     */
    int updateStepSequence(@Param("stepId") Long stepId, @Param("sequence") Integer sequence);

    @Select("SELECT COALESCE(SUM(completed_quantity), 0) FROM mes_production_task " +
            "WHERE status = 'completed' AND DATE(update_time) = CURDATE() AND is_deleted = 0")
    Integer selectTodayOutputSimple();
}
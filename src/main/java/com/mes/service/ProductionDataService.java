package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.ProductionData;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductionDataService extends IService<ProductionData> {

    /**
     * 条件分页查询生产数据
     */
    IPage<ProductionData> getDataByCondition(Page<ProductionData> page, Long taskId, String dataType, LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取实时数据
     */
    List<ProductionData> getRealTimeData(Long resourceId);

    /**
     * 批量插入数据
     */
    boolean batchInsertData(List<ProductionData> dataList);

    /**
     * 根据任务ID查询数据
     */
    List<ProductionData> getDataByTaskId(Long taskId);

    /**
     * 获取最新数据
     */
    ProductionData getLatestData(Long taskId, String dataType);
}
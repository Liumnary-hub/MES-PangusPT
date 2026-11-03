package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.ProductionData;
import com.mes.mapper.ProductionDataMapper;
import com.mes.service.ProductionDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductionDataServiceImpl extends ServiceImpl<ProductionDataMapper, ProductionData>
        implements ProductionDataService {

    @Override
    public Page<ProductionData> getDataByCondition(Page<ProductionData> page, Long taskId, String dataType, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectDataByCondition(page, taskId, dataType, startTime, endTime);
    }

    @Override
    public List<ProductionData> getRealTimeData(Long resourceId) {
        // 获取最近15分钟的数据作为实时数据
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        return baseMapper.selectRecentDataByResource(resourceId, fifteenMinutesAgo);
    }

    @Override
    @Transactional
    public boolean batchInsertData(List<ProductionData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return false;
        }

        try {
            // 为每条数据设置采集时间
            for (ProductionData data : dataList) {
                if (data.getCollectionTime() == null) {
                    data.setCollectionTime(LocalDateTime.now());
                }
            }

            int result = baseMapper.batchInsert(dataList);
            return result > 0;
        } catch (Exception e) {
            log.error("批量插入生产数据失败", e);
            return false;
        }
    }

    @Override
    public List<ProductionData> getDataByTaskId(Long taskId) {
        LambdaQueryWrapper<ProductionData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductionData::getTaskId, taskId)
                .orderByDesc(ProductionData::getCollectionTime);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ProductionData getLatestData(Long taskId, String dataType) {
        return baseMapper.selectLatestData(taskId, dataType);
    }

    /**
     * 获取指定时间段内的产量数据汇总
     */
    public Map<String, Object> getProductionSummary(Long taskId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> summary = baseMapper.selectProductionSummary(taskId, startTime, endTime);
        if (summary == null) {
            summary = new HashMap<>();
            summary.put("totalQuantity", 0);
            summary.put("dataCount", 0);
        }
        summary.put("startTime", startTime);
        summary.put("endTime", endTime);
        return summary;
    }

    /**
     * 获取质量数据统计
     */
    public Map<String, Object> getQualityStatistics(Long taskId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Map<String, Object>> qualityStats = baseMapper.selectQualityStatistics(taskId, startTime, endTime);

        int qualifiedCount = 0;
        int totalCount = 0;

        for (Map<String, Object> stat : qualityStats) {
            String dataValue = (String) stat.get("data_value");
            Long count = (Long) stat.get("count");
            totalCount += count;
            if ("qualified".equals(dataValue)) {
                qualifiedCount += count;
            }
        }

        double qualifiedRate = totalCount > 0 ? (double) qualifiedCount / totalCount * 100 : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("qualifiedCount", qualifiedCount);
        stats.put("unqualifiedCount", totalCount - qualifiedCount);
        stats.put("totalCount", totalCount);
        stats.put("qualifiedRate", Math.round(qualifiedRate * 100.0) / 100.0);

        return stats;
    }

    /**
     * 获取设备状态统计
     */
    public List<Map<String, Object>> getEquipmentStatusStats(Long taskId, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectEquipmentStatusStats(taskId, startTime, endTime);
    }

    /**
     * 获取生产趋势数据
     */
    public List<Map<String, Object>> getProductionTrend(Long taskId, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectProductionTrend(taskId, startTime, endTime);
    }

    /**
     * 获取设备运行效率统计
     */
    public List<Map<String, Object>> getEquipmentEfficiencyStats(Long resourceId, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectEquipmentEfficiencyStats(resourceId, startTime, endTime);
    }

    /**
     * 获取生产异常统计
     */
    public List<Map<String, Object>> getProductionAnomalyStats(Long taskId, LocalDateTime startTime, LocalDateTime endTime) {
        return baseMapper.selectProductionAnomalyStats(taskId, startTime, endTime);
    }

    /**
     * 获取综合生产统计报告
     */
    public Map<String, Object> getComprehensiveProductionReport(Long taskId, LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> report = new HashMap<>();

        // 产量统计
        Map<String, Object> productionSummary = getProductionSummary(taskId, startTime, endTime);
        report.put("productionSummary", productionSummary);

        // 质量统计
        Map<String, Object> qualityStats = getQualityStatistics(taskId, startTime, endTime);
        report.put("qualityStats", qualityStats);

        // 设备状态统计
        List<Map<String, Object>> equipmentStats = getEquipmentStatusStats(taskId, startTime, endTime);
        report.put("equipmentStats", equipmentStats);

        // 生产趋势
        List<Map<String, Object>> productionTrend = getProductionTrend(taskId, startTime, endTime);
        report.put("productionTrend", productionTrend);

        // 异常统计
        List<Map<String, Object>> anomalyStats = getProductionAnomalyStats(taskId, startTime, endTime);
        report.put("anomalyStats", anomalyStats);

        report.put("taskId", taskId);
        report.put("startTime", startTime);
        report.put("endTime", endTime);
        report.put("reportGeneratedTime", LocalDateTime.now());

        return report;
    }
}
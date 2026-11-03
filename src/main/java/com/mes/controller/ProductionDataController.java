package com.mes.controller;

import com.mes.entity.ProductionData;
import com.mes.service.DeviceDataPublisher;
import com.mes.service.ProductionDataService;
import com.mes.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 设备生产数据采集接口
 */
@RestController
@RequestMapping("/api/production-data")
@RequiredArgsConstructor
public class ProductionDataController {

    private static final int MAX_BATCH_SIZE = 1000;

    private final ProductionDataService productionDataService;
    private final DeviceDataPublisher deviceDataPublisher;

    /**
     * 批量上报采集数据，异步入库
     */
    @PostMapping("/batch")
    public Result<String> batchReport(@RequestBody List<ProductionData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return Result.error(400, "数据不能为空");
        }
        if (dataList.size() > MAX_BATCH_SIZE) {
            return Result.error(400, "单次最多上报 " + MAX_BATCH_SIZE + " 条");
        }
        LocalDateTime now = LocalDateTime.now();
        for (ProductionData data : dataList) {
            if (data.getTaskId() == null) {
                return Result.error(400, "taskId 不能为空");
            }
            if (data.getDataType() == null || data.getDataValue() == null) {
                return Result.error(400, "dataType 和 dataValue 不能为空");
            }
            if (data.getCollectionTime() == null) {
                data.setCollectionTime(now);
            }
        }
        deviceDataPublisher.publish(dataList);
        return Result.success("accepted", "数据已接收，异步入库");
    }

    @GetMapping("/task/{taskId}")
    public Result<List<ProductionData>> getByTaskId(@PathVariable Long taskId) {
        return Result.success(productionDataService.getDataByTaskId(taskId));
    }

    @GetMapping("/latest")
    public Result<ProductionData> getLatest(@RequestParam Long taskId, @RequestParam String dataType) {
        return Result.success(productionDataService.getLatestData(taskId, dataType));
    }

    @GetMapping("/resource/{resourceId}/realtime")
    public Result<List<ProductionData>> getRealTime(@PathVariable Long resourceId) {
        return Result.success(productionDataService.getRealTimeData(resourceId));
    }
}

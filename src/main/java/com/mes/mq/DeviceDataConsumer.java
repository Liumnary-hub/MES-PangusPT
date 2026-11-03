package com.mes.mq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.config.RabbitConfig;
import com.mes.entity.ProductionData;
import com.mes.service.ProductionDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设备数据消费者：批量入库
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceDataConsumer {

    private final ProductionDataService productionDataService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = RabbitConfig.DEVICE_DATA_QUEUE)
    public void consume(String payload) {
        try {
            List<ProductionData> dataList = objectMapper.readValue(payload, new TypeReference<>() {
            });
            if (dataList == null || dataList.isEmpty()) {
                return;
            }
            boolean saved = productionDataService.batchInsertData(dataList);
            log.info("设备数据入库 {} 条, result={}", dataList.size(), saved);
        } catch (Exception e) {
            log.error("设备数据入库失败, payload={}", payload, e);
        }
    }
}

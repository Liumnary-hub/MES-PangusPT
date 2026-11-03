package com.mes.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mes.config.RabbitConfig;
import com.mes.entity.ProductionData;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备生产数据发布：写入 RabbitMQ，由消费者异步入库
 */
@Service
@RequiredArgsConstructor
public class DeviceDataPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void publish(List<ProductionData> dataList) {
        try {
            String payload = objectMapper.writeValueAsString(new ArrayList<>(dataList));
            rabbitTemplate.convertAndSend(
                    RabbitConfig.DEVICE_DATA_EXCHANGE,
                    RabbitConfig.DEVICE_DATA_ROUTING_KEY,
                    payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("设备数据序列化失败", e);
        }
    }
}

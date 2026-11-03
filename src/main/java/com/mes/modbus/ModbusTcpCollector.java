package com.mes.modbus;

import com.ghgande.j2mod.modbus.io.ModbusTCPTransaction;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersResponse;
import com.ghgande.j2mod.modbus.net.TCPMasterConnection;
import com.mes.entity.ProductionData;
import com.mes.service.DeviceDataPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Modbus TCP 设备数据采集器。
 * 寄存器约定：0-累计产量 1-累计合格数 2-设备状态 3-任务ID
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "mes.modbus.enabled", havingValue = "true")
public class ModbusTcpCollector {

    private final DeviceDataPublisher deviceDataPublisher;
    private final Map<Long, Integer> lastProduced = new ConcurrentHashMap<>();

    @Value("${mes.modbus.host:localhost}")
    private String host;

    @Value("${mes.modbus.port:5020}")
    private int port;

    @Value("${mes.modbus.unit-id:1}")
    private int unitId;

    @Value("${mes.modbus.task-id:1}")
    private long defaultTaskId;

    @Scheduled(fixedDelayString = "${mes.modbus.poll-interval-ms:5000}",
            initialDelayString = "${mes.modbus.initial-delay-ms:3000}")
    public void collect() {
        TCPMasterConnection connection = null;
        try {
            connection = new TCPMasterConnection(InetAddress.getByName(host));
            connection.setPort(port);
            connection.setTimeout(2000);
            connection.connect();

            ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(0, 4);
            request.setUnitID(unitId);

            ModbusTCPTransaction transaction = new ModbusTCPTransaction(connection);
            transaction.setRequest(request);
            transaction.execute();

            ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
            int produced = response.getRegisterValue(0);
            int qualified = response.getRegisterValue(1);
            int status = response.getRegisterValue(2);
            int taskIdRegister = response.getRegisterValue(3);
            long taskId = taskIdRegister > 0 ? taskIdRegister : defaultTaskId;

            List<ProductionData> dataList = new ArrayList<>();
            int previous = lastProduced.getOrDefault(taskId, produced);
            int delta = produced >= previous ? produced - previous : produced;
            lastProduced.put(taskId, produced);
            if (delta > 0) {
                dataList.add(build(taskId, "quantity", String.valueOf(delta)));
            }
            dataList.add(build(taskId, "quality", qualified >= produced ? "qualified" : "unqualified"));
            dataList.add(build(taskId, "equipment", statusName(status)));

            deviceDataPublisher.publish(dataList);
            log.debug("Modbus 采集成功 taskId={} produced={} delta={} status={}", taskId, produced, delta, status);
        } catch (Exception e) {
            log.warn("Modbus 采集失败 {}:{} - {}", host, port, e.getMessage());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    private ProductionData build(long taskId, String dataType, String value) {
        ProductionData data = new ProductionData();
        data.setTaskId(taskId);
        data.setDataType(dataType);
        data.setDataValue(value);
        data.setCollectionTime(LocalDateTime.now());
        data.setRemark("modbus-tcp");
        return data;
    }

    private String statusName(int status) {
        return switch (status) {
            case 1 -> "RUNNING";
            case 2 -> "IDLE";
            case 3 -> "FAULT";
            default -> "UNKNOWN";
        };
    }
}

package com.mes.modbus;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最小 Modbus TCP 从站模拟器（功能码 0x03），用于本地联调采集器。
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "mes.modbus.simulator", havingValue = "true")
public class ModbusSlaveSimulator implements Runnable {

    private static final int FUNCTION_READ_HOLDING_REGISTERS = 0x03;

    @Value("${mes.modbus.simulator-port:5020}")
    private int port;

    @Value("${mes.modbus.task-id:1}")
    private int taskId;

    private final AtomicInteger produced = new AtomicInteger(0);
    private ServerSocket serverSocket;
    private Thread serverThread;
    private volatile boolean running = true;

    @PostConstruct
    public void start() {
        serverThread = new Thread(this, "modbus-slave-simulator");
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            serverSocket = server;
            log.info("Modbus TCP 模拟从站已启动，端口 {}", port);
            while (running) {
                try (Socket socket = server.accept()) {
                    handle(socket);
                } catch (IOException e) {
                    if (running) {
                        log.warn("Modbus 模拟从站连接处理失败: {}", e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            log.error("Modbus TCP 模拟从站启动失败，端口 {}: {}", port, e.getMessage());
        }
    }

    private void handle(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        int transactionId = in.readUnsignedShort();
        int protocolId = in.readUnsignedShort();
        int length = in.readUnsignedShort();
        int unitId = in.readUnsignedByte();
        int function = in.readUnsignedByte();
        int startAddress = in.readUnsignedShort();
        int quantity = in.readUnsignedShort();

        if (protocolId != 0 || function != FUNCTION_READ_HOLDING_REGISTERS) {
            log.warn("不支持的 Modbus 请求 protocol={} function={}", protocolId, function);
            return;
        }

        int current = produced.addAndGet(2);
        int qualified = current - current / 20;
        int[] registers = {current, qualified, 1, taskId};

        out.writeShort(transactionId);
        out.writeShort(0);
        out.writeShort(3 + quantity * 2);
        out.writeByte(unitId);
        out.writeByte(function);
        out.writeByte(quantity * 2);
        for (int i = 0; i < quantity; i++) {
            int index = startAddress + i;
            int value = index < registers.length ? registers[index] : 0;
            out.writeShort(value & 0xFFFF);
        }
        out.flush();
        log.debug("Modbus 响应 tx={} start={} quantity={}", transactionId, startAddress, quantity);
    }

    @PreDestroy
    public void stop() {
        running = false;
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ignored) {
                // 关闭时的异常无需处理
            }
        }
    }
}

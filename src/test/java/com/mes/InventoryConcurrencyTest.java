package com.mes;

import com.mes.entity.DefectDetail;
import com.mes.entity.Inventory;
import com.mes.mapper.DefectDetailMapper;
import com.mes.mapper.InventoryMapper;
import com.mes.service.BatchTraceService;
import com.mes.service.InventoryService;
import com.mes.vo.BatchTraceVO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 库存并发与追溯缺陷节点集成测试。
 * 依赖本机 MySQL、Redis、RabbitMQ（docker compose up -d）。
 */
@SpringBootTest
class InventoryConcurrencyTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private BatchTraceService batchTraceService;

    @Autowired
    private DefectDetailMapper defectDetailMapper;

    private final List<Long> inventoryIds = new ArrayList<>();
    private final List<Long> defectIds = new ArrayList<>();

    @AfterEach
    void cleanup() {
        inventoryIds.forEach(inventoryMapper::deleteById);
        inventoryIds.clear();
        defectIds.forEach(defectDetailMapper::deleteById);
        defectIds.clear();
    }

    @Test
    void concurrentLockShouldNotOverLock() throws Exception {
        long materialId = System.nanoTime();
        Inventory inventory = createInventory(materialId, new BigDecimal("100"), BigDecimal.ZERO);

        int success = runConcurrently(20, () -> inventoryService.lockStock(
                materialId, 1L, inventory.getBatchNumber(), BigDecimal.TEN));

        Inventory after = inventoryMapper.selectById(inventory.getId());
        assertEquals(10, success, "100 库存按 10 锁定，只应成功 10 次");
        assertEquals(0, after.getCurrentQuantity().compareTo(new BigDecimal("100")));
        assertEquals(0, after.getLockedQuantity().compareTo(new BigDecimal("100")));
    }

    @Test
    void concurrentDeductShouldNotGoNegative() throws Exception {
        long materialId = System.nanoTime();
        Inventory inventory = createInventory(materialId, new BigDecimal("50"), new BigDecimal("50"));

        int success = runConcurrently(10, () -> inventoryService.deductStock(
                materialId, 1L, inventory.getBatchNumber(), BigDecimal.TEN));

        Inventory after = inventoryMapper.selectById(inventory.getId());
        assertEquals(5, success, "50 现存量按 10 扣减，只应成功 5 次");
        assertEquals(0, after.getCurrentQuantity().compareTo(BigDecimal.ZERO));
        assertEquals(0, after.getLockedQuantity().compareTo(BigDecimal.ZERO));
    }

    @Test
    void traceShouldContainDefectByBatch() {
        String batchNo = "TRACE-" + System.nanoTime();
        DefectDetail defect = new DefectDetail();
        defect.setBatchNo(batchNo);
        defect.setDefectCode("D001");
        defect.setDefectName("表面划痕");
        defect.setDefectQuantity(2);
        defect.setCreateTime(LocalDateTime.now());
        defectDetailMapper.insert(defect);
        defectIds.add(defect.getId());

        BatchTraceVO trace = batchTraceService.traceByBatchNo(batchNo);

        assertEquals(1, trace.getDefects().size());
        assertEquals("D001", trace.getDefects().get(0).getDefectCode());
    }

    private Inventory createInventory(long materialId, BigDecimal current, BigDecimal locked) {
        Inventory inventory = new Inventory();
        inventory.setMaterialId(materialId);
        inventory.setMaterialCode("TEST-" + materialId);
        inventory.setLocationId(1L);
        inventory.setLocationCode("L001");
        inventory.setBatchNumber("BATCH-" + materialId);
        inventory.setCurrentQuantity(current);
        inventory.setLockedQuantity(locked);
        inventory.setQualityStatus("qualified");
        inventory.setLastUpdateTime(LocalDateTime.now());
        inventoryMapper.insert(inventory);
        inventoryIds.add(inventory.getId());
        return inventory;
    }

    private int runConcurrently(int threads, ThrowingRunnable task) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch ready = new CountDownLatch(threads);
        CountDownLatch start = new CountDownLatch(1);
        AtomicInteger success = new AtomicInteger();
        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < threads; i++) {
            futures.add(pool.submit(() -> {
                ready.countDown();
                start.await();
                try {
                    task.run();
                    success.incrementAndGet();
                } catch (Exception ignored) {
                    // 库存不足时业务方法抛异常，属于预期失败
                }
                return null;
            }));
        }

        ready.await();
        start.countDown();
        for (Future<?> future : futures) {
            future.get(30, TimeUnit.SECONDS);
        }
        pool.shutdown();
        return success.get();
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}

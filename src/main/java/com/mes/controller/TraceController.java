package com.mes.controller;

import com.mes.service.BatchTraceService;
import com.mes.vo.BatchTraceVO;
import com.mes.vo.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 批次全流程追溯
 */
@RestController
@RequestMapping("/api/trace")
@RequiredArgsConstructor
public class TraceController {

    private final BatchTraceService batchTraceService;

    @GetMapping("/batch/{batchNo}")
    public Result<BatchTraceVO> traceByBatchNo(@PathVariable String batchNo) {
        BatchTraceVO trace = batchTraceService.traceByBatchNo(batchNo);
        boolean empty = trace.getInspectionTasks().isEmpty()
                && trace.getOrders().isEmpty()
                && trace.getProcessTasks().isEmpty()
                && trace.getPickLists().isEmpty()
                && trace.getPickedMaterials().isEmpty()
                && trace.getInventoryLots().isEmpty();
        if (empty) {
            return Result.error(404, "未找到批次 " + batchNo + " 的相关记录");
        }
        return Result.success(trace);
    }
}

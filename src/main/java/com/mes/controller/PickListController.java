package com.mes.controller;

import com.mes.dto.PickListCreateDTO;
import com.mes.dto.PickListExecuteDTO;


import com.mes.dto.PickListQueryDTO;
import com.mes.service.PickListService;
import com.mes.util.TableDataInfo;
import com.mes.vo.PickListVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 领料单管理控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/picklist")
public class PickListController {

    private final PickListService pickListService;

    /**
     * 分页查询领料单列表
     */
    @GetMapping("/list")
    public TableDataInfo<PickListVO> list(PickListQueryDTO queryDTO) {
        return pickListService.selectPickListPage(queryDTO);
    }

    /**
     * 查询领料单列表
     */
    @GetMapping("/all")
    public List<PickListVO> all(PickListQueryDTO queryDTO) {
        return pickListService.selectPickListList(queryDTO);
    }

    /**
     * 获取领料单详细信息
     */
    @GetMapping("/{id}")
    public PickListVO getInfo(@PathVariable Long id) {
        return pickListService.getPickListDetail(id);
    }

    /**
     * 根据工单创建领料单
     */
    @PostMapping("/fromWorkOrder/{workOrderId}")
    public boolean createFromWorkOrder(@PathVariable Long workOrderId) {
        return pickListService.createPickListFromWorkOrder(workOrderId);
    }

    /**
     * 创建领料单
     */
    @PostMapping
    public boolean create(@Validated @RequestBody PickListCreateDTO createDTO) {
        return pickListService.createPickList(createDTO);
    }

    /**
     * 执行领料
     */
    @PostMapping("/execute")
    public boolean execute(@Validated @RequestBody PickListExecuteDTO executeDTO) {
        return pickListService.executePickList(executeDTO);
    }

    /**
     * 完成领料单
     */
    @PostMapping("/{id}/complete")
    public boolean complete(@PathVariable Long id) {
        return pickListService.completePickList(id);
    }

    /**
     * 取消领料单
     */
    @PostMapping("/{id}/cancel")
    public boolean cancel(@PathVariable Long id, @RequestParam String reason) {
        return pickListService.cancelPickList(id, reason);
    }

    /**
     * 打印领料单
     */
    @PostMapping("/{id}/print")
    public boolean print(@PathVariable Long id) {
        return pickListService.printPickList(id);
    }

    /**
     * 根据工单查询领料单
     */
    /**
     * 根据工单查询领料单
     */
    @GetMapping("/workOrder/{workOrderId}")
    public List<PickListVO> getByWorkOrder(@PathVariable Long workOrderId) {
        PickListQueryDTO queryDTO = new PickListQueryDTO();
        queryDTO.setWorkOrderId(workOrderId);
        return pickListService.selectPickListList(queryDTO);
    }
}
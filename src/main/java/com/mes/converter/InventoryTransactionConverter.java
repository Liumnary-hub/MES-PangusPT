package com.mes.converter;

import com.mes.entity.InventoryTransaction;
import com.mes.enums.ReferenceType;
import com.mes.enums.TransactionType;
import com.mes.vo.InventoryTransactionVO;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 库存交易记录转换工具类
 */
public class InventoryTransactionConverter {

    /**
     * Entity 转 VO
     */
    public static InventoryTransactionVO entityToVo(InventoryTransaction entity) {
        if (entity == null) {
            return null;
        }

        InventoryTransactionVO vo = new InventoryTransactionVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置描述字段
        vo.setTransactionTypeDesc(getTransactionTypeDesc(entity.getTransactionType()));
        vo.setReferenceTypeDesc(getReferenceTypeDesc(entity.getReferenceType()));

        // 设置交易方向
        vo.setDirection(getTransactionDirection(entity.getTransactionType()));

        // 设置是否影响库存
        vo.setAffectInventory(isAffectInventory(entity.getTransactionType()));

        return vo;
    }

    /**
     * Entity 列表转 VO 列表
     */
    public static List<InventoryTransactionVO> entityListToVoList(List<InventoryTransaction> entityList) {
        if (entityList == null) {
            return null;
        }

        return entityList.stream()
                .map(InventoryTransactionConverter::entityToVo)
                .collect(Collectors.toList());
    }

    /**
     * 获取交易类型描述
     */
    private static String getTransactionTypeDesc(String transactionType) {
        if (transactionType == null) {
            return "";
        }
        try {
            return TransactionType.valueOf(transactionType).getDescription();
        } catch (IllegalArgumentException e) {
            return transactionType;
        }
    }

    /**
     * 获取关联类型描述
     */
    private static String getReferenceTypeDesc(String referenceType) {
        if (referenceType == null) {
            return "";
        }
        try {
            return ReferenceType.valueOf(referenceType).getDescription();
        } catch (IllegalArgumentException e) {
            return referenceType;
        }
    }

    /**
     * 获取交易方向：1-入库，-1-出库，0-转移
     */
    private static Integer getTransactionDirection(String transactionType) {
        if (transactionType == null) {
            return 0;
        }
        switch (transactionType) {
            case "IN":
                return 1;
            case "OUT":
                return -1;
            case "TRANSFER":
            case "ADJUST":
            default:
                return 0;
        }
    }

    /**
     * 判断是否影响库存总量
     */
    private static Boolean isAffectInventory(String transactionType) {
        if (transactionType == null) {
            return false;
        }
        return "IN".equals(transactionType) || "OUT".equals(transactionType);
    }
}
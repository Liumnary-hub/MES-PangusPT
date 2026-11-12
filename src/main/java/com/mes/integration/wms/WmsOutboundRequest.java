package com.mes.integration.wms;

import lombok.Data;

/**
 * 发送给 WMS /api/open/inventory/outbound 的领料出库请求。
 */
@Data
public class WmsOutboundRequest {

    //幂等键：MES-PICKLIST-{领料单ID}-ITEM-{明细ID}
    private String requestId;

    private String commodityId;
    private String commodityCode;
    private String commodityName;

    private String warehouseId;
    private String warehouseCode;
    private String warehouseName;

    private Integer quantity;

    private String batchNo;

    private String sourceType;
    private String sourceNo;
    private String sourceItemNo;

    private String description;
}

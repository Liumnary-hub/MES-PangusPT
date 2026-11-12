package com.mes.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * WMS 集成配置，前缀 mes.wms。
 */
@Data
@Component
@ConfigurationProperties(prefix = "mes.wms")
public class WmsProperties {

    //关闭后领料完成只扣 MES 本地库存，不调用 WMS
    private boolean enabled = true;

    private String baseUrl = "http://localhost:8088";

    private String apiKey = "mes-wms-dev-key";

    //完工入库写入的WMS仓库编码
    private String finishedGoodsWarehouseCode = "FG01";

    private int connectTimeoutMs = 3000;

    private int readTimeoutMs = 5000;
}

package com.mes.integration.wms;

import com.mes.common.BusinessException;
import com.mes.config.WmsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * 调用 WMS 库存开放接口。
 * requestId 幂等：重复提交不会重复扣减/增加 WMS 库存。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WmsInventoryClient {

    private static final String OUTBOUND_PATH = "/api/open/inventory/outbound";
    private static final String INBOUND_PATH = "/api/open/inventory/inbound";

    private final RestTemplate wmsRestTemplate;
    private final WmsProperties properties;

    public void outbound(WmsOutboundRequest request) {
        post(OUTBOUND_PATH, request, "出库");
    }

    public void inbound(WmsInboundRequest request) {
        post(INBOUND_PATH, request, "入库");
    }

    private void post(String path, WmsOutboundRequest request, String action) {
        if (!properties.isEnabled()) {
            log.warn("WMS 集成已关闭，跳过{}: requestId={}", action, request.getRequestId());
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Integration-Key", properties.getApiKey());

        String url = trimTrailingSlash(properties.getBaseUrl()) + path;
        try {
            ResponseEntity<WmsApiResponse> response = wmsRestTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    WmsApiResponse.class);

            WmsApiResponse body = response.getBody();
            if (!response.getStatusCode().is2xxSuccessful() || body == null || !body.isSuccess()) {
                String message = body != null ? body.errorMessage() : response.getStatusCode().toString();
                throw new BusinessException("WMS" + action + "失败: " + message);
            }
            log.info("WMS{}成功 requestId={}, data={}", action, request.getRequestId(), body.getData());
        } catch (RestClientException e) {
            throw new BusinessException("调用WMS" + action + "接口失败: " + e.getMessage(), e);
        }
    }

    private String trimTrailingSlash(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}

package com.mes.integration.wms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

/**
 * WMS 统一响应：{code, status, msg, data}。
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WmsApiResponse {

    private Integer code;

    private Boolean status;

    private String msg;

    private Map<String, Object> data;

    public boolean isSuccess() {
        return Integer.valueOf(200).equals(code) && Boolean.TRUE.equals(status);
    }

    public String errorMessage() {
        if (msg != null && !msg.isEmpty()) {
            return msg;
        }
        return data != null && data.get("message") != null ? String.valueOf(data.get("message")) : "未知错误";
    }
}

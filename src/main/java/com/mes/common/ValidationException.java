package com.mes.common;

import com.mes.common.BusinessException;

/**
 * 验证异常
 */
public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(400, message);
    }

    public ValidationException(String message, Throwable cause) {
        super(400, message, cause);
    }
}
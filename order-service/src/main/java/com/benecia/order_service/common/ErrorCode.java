package com.benecia.order_service.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus status() { return status; }
    public String defaultMessage() { return defaultMessage; }
}

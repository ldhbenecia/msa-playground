package com.benecia.order_service.dto;

public record CreateOrderRequest(
        String productId,
        Integer qty,
        Integer unitPrice,
        String userId
) {
}

package com.benecia.order_service.dto;

import java.time.LocalDateTime;

public record OrderResponse(
        Long orderId,
        String productId,
        Integer qty,
        Integer unitPrice,
        Integer totalPrice,
        String userId,
        LocalDateTime createdAt
) {
}

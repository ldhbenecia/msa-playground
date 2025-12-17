package com.benecia.order_service.dto;

public record PointsFailed(
        Long orderId,
        String userId,
        String reason
) {
}

package com.benecia.order_service.client;

public record UserResponse(
        String userId,
        String name,
        String email
) {
}

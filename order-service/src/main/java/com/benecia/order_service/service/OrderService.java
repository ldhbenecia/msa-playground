package com.benecia.order_service.service;

import com.benecia.order_service.dto.CreateOrderRequest;
import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.repository.OrderEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderWriter orderWriter;
    private final OrderReader orderReader;

    public OrderResponse createOrder(CreateOrderRequest request) {
        return orderWriter.createOrder(request);
    }

    public List<OrderResponse> getOrdersByUserId(String userId) {
        return orderReader.getOrdersByUserId(userId);
    }

    public void cancelOrder(Long orderId) {
        orderWriter.cancelOrder(orderId);
    }
}

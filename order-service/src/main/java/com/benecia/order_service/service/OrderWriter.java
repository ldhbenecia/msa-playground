package com.benecia.order_service.service;

import com.benecia.order_service.client.UserClient;
import com.benecia.order_service.common.AppException;
import com.benecia.order_service.common.ErrorCode;
import com.benecia.order_service.dto.CreateOrderRequest;
import com.benecia.order_service.dto.OrderResponse;
import com.benecia.order_service.repository.OrderEntity;
import com.benecia.order_service.repository.OrderJpaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderWriter {

    private final OrderJpaRepository orderJpaRepository;
    private final StreamBridge streamBridge;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        OrderEntity orderEntity = new OrderEntity(
                request.productId(),
                request.qty(),
                request.unitPrice(),
                request.userId()
        );

        orderJpaRepository.save(orderEntity);
        log.info("Order saved to DB: {}", orderEntity.getId());

        OrderResponse response = new OrderResponse(
                orderEntity.getId(),
                orderEntity.getProductId(),
                orderEntity.getQty(),
                orderEntity.getUnitPrice(),
                orderEntity.getTotalPrice(),
                orderEntity.getUserId(),
                orderEntity.getCreatedAt()
        );

        try {
            log.info("Sending order-created event to Kafka: {}", response);
            streamBridge.send("orderCreated-out-0", response);
            log.info("Event successfully sent to Kafka.");
        } catch (Exception e) {
            log.error("Failed to send order-created event to Kafka", e);
        }

        return response;
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity orderEntity = orderJpaRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND, "Order not found for orderId: " + orderId));

        orderEntity.cancel();

        log.info("Order status set to CANCELLED for orderId: {}", orderId);
    }
}

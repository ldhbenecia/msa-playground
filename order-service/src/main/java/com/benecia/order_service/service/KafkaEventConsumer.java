package com.benecia.order_service.service;

import com.benecia.order_service.dto.PointsFailed;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaEventConsumer {

    private final OrderService orderService;

    @Bean
    public Consumer<PointsFailed> pointsFailed() {
        return failedDto -> {
            log.warn("Received points-failed event (SAGA Compensation): {}", failedDto);

            orderService.cancelOrder(failedDto.orderId());
        };
    }
}

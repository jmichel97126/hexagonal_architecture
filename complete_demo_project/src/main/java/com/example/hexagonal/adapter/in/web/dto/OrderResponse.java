package com.example.hexagonal.adapter.in.web.dto;

import com.example.hexagonal.application.query.OrderView;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        String orderId,
        String customerId,
        String status,
        Instant createdAt,
        BigDecimal totalAmount,
        String currency,
        long version,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(OrderView view) {
        return new OrderResponse(
                view.orderId(),
                view.customerId(),
                view.status(),
                view.createdAt(),
                view.totalAmount(),
                view.currency(),
                view.version(),
                view.items().stream()
                        .map(item -> new OrderItemResponse(
                                item.productCode(),
                                item.quantity(),
                                item.unitPrice(),
                                item.subtotal(),
                                item.currency()
                        ))
                        .toList()
        );
    }

    public record OrderItemResponse(
            String productCode,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal,
            String currency
    ) {
    }
}

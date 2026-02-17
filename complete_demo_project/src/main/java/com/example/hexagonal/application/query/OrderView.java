package com.example.hexagonal.application.query;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderView(
        String orderId,
        String customerId,
        String status,
        Instant createdAt,
        BigDecimal totalAmount,
        String currency,
        long version,
        List<OrderItemView> items
) {
    public record OrderItemView(
            String productCode,
            int quantity,
            BigDecimal unitPrice,
            BigDecimal subtotal,
            String currency
    ) {
    }
}

package com.example.hexagonal.application.command;

import java.math.BigDecimal;

public record CreateOrderItemCommand(
        String productCode,
        int quantity,
        BigDecimal unitPrice,
        String currency
) {
}

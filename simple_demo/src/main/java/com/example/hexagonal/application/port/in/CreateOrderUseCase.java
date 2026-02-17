package com.example.hexagonal.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

public interface CreateOrderUseCase {

    UUID create(CreateOrderCommand command);

    record CreateOrderCommand(String customerName, BigDecimal totalAmount) {
    }
}
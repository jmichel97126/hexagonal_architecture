package com.example.hexagonal.application.command;

import java.util.List;

public record CreateOrderCommand(
        String customerId,
        List<CreateOrderItemCommand> items
) {
}

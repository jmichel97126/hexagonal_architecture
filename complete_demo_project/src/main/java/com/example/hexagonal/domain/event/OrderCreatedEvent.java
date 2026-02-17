package com.example.hexagonal.domain.event;

import java.time.Instant;

public record OrderCreatedEvent(
        String orderId,
        String customerId,
        Instant occurredAt
) implements DomainEvent {

    @Override
    public String type() {
        return "ORDER_CREATED";
    }
}

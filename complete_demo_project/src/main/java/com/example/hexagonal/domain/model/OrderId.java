package com.example.hexagonal.domain.model;

import java.util.Objects;

public record OrderId(String value) {

    public OrderId {
        Objects.requireNonNull(value, "orderId is required");
        if (value.isBlank()) {
            throw new IllegalArgumentException("orderId cannot be blank");
        }
    }
}

package com.example.hexagonal.domain.model;

import java.util.Objects;

public record CustomerId(String value) {

    public CustomerId {
        Objects.requireNonNull(value, "customerId is required");
        if (value.isBlank()) {
            throw new IllegalArgumentException("customerId cannot be blank");
        }
    }
}

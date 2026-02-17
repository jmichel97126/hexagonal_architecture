package com.example.hexagonal.domain.model;

import java.util.Objects;

public record OrderItem(String productCode, int quantity, Money unitPrice) {

    public OrderItem {
        Objects.requireNonNull(productCode, "productCode is required");
        Objects.requireNonNull(unitPrice, "unitPrice is required");

        if (productCode.isBlank()) {
            throw new IllegalArgumentException("productCode cannot be blank");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }
    }

    public Money subtotal() {
        return unitPrice.multiply(quantity);
    }
}

package com.example.hexagonal.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class Order {

    private final UUID id;
    private final String customerName;
    private final BigDecimal totalAmount;
    private final Instant createdAt;

    private Order(UUID id, String customerName, BigDecimal totalAmount, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.customerName = validateCustomerName(customerName);
        this.totalAmount = validateTotalAmount(totalAmount);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static Order create(String customerName, BigDecimal totalAmount) {
        return new Order(UUID.randomUUID(), customerName, totalAmount, Instant.now());
    }

    public UUID id() {
        return id;
    }

    public String customerName() {
        return customerName;
    }

    public BigDecimal totalAmount() {
        return totalAmount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    private static String validateCustomerName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("customerName cannot be blank");
        }
        return value;
    }

    private static BigDecimal validateTotalAmount(BigDecimal value) {
        if (value == null || value.signum() <= 0) {
            throw new IllegalArgumentException("totalAmount must be > 0");
        }
        return value;
    }
}
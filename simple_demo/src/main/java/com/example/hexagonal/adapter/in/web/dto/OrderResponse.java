package com.example.hexagonal.adapter.in.web.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        String customerName,
        BigDecimal totalAmount,
        Instant createdAt
) {
}
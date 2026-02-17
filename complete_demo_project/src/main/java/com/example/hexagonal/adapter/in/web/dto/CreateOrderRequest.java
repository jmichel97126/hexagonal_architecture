package com.example.hexagonal.adapter.in.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        @NotBlank String customerId,
        @NotEmpty List<@Valid ItemRequest> items
) {
    public record ItemRequest(
            @NotBlank String productCode,
            @Positive int quantity,
            @NotNull @Positive BigDecimal unitPrice,
            @NotBlank String currency
    ) {
    }
}

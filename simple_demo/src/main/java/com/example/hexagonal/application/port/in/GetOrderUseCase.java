package com.example.hexagonal.application.port.in;

import com.example.hexagonal.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface GetOrderUseCase {

    Optional<Order> getById(UUID id);
}
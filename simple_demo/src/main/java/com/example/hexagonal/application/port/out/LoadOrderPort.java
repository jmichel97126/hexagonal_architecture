package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.model.Order;

import java.util.Optional;
import java.util.UUID;

public interface LoadOrderPort {

    Optional<Order> loadById(UUID id);
}
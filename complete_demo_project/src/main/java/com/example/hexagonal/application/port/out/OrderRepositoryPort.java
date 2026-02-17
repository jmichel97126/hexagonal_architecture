package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderId;

import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(OrderId orderId);
}

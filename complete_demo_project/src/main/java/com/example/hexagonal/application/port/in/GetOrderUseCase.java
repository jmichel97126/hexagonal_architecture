package com.example.hexagonal.application.port.in;

import com.example.hexagonal.application.query.OrderView;

import java.util.Optional;

public interface GetOrderUseCase {

    Optional<OrderView> findById(String orderId);
}

package com.example.hexagonal.application.port.in;

import com.example.hexagonal.application.query.OrderView;

public interface CancelOrderUseCase {

    OrderView cancel(String orderId);
}

package com.example.hexagonal.application.port.in;

import com.example.hexagonal.application.command.CreateOrderCommand;
import com.example.hexagonal.application.query.OrderView;

public interface CreateOrderUseCase {

    OrderView create(CreateOrderCommand command);
}

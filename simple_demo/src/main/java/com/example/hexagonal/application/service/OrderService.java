package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.CreateOrderUseCase;
import com.example.hexagonal.application.port.in.GetOrderUseCase;
import com.example.hexagonal.application.port.out.LoadOrderPort;
import com.example.hexagonal.application.port.out.SaveOrderPort;
import com.example.hexagonal.domain.model.Order;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService implements CreateOrderUseCase, GetOrderUseCase {

    private final SaveOrderPort saveOrderPort;
    private final LoadOrderPort loadOrderPort;

    public OrderService(SaveOrderPort saveOrderPort, LoadOrderPort loadOrderPort) {
        this.saveOrderPort = saveOrderPort;
        this.loadOrderPort = loadOrderPort;
    }

    @Override
    public UUID create(CreateOrderCommand command) {
        Order order = Order.create(command.customerName(), command.totalAmount());
        saveOrderPort.save(order);
        return order.id();
    }

    @Override
    public Optional<Order> getById(UUID id) {
        return loadOrderPort.loadById(id);
    }
}
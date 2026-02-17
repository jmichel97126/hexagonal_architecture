package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.adapter.in.web.dto.CreateOrderRequest;
import com.example.hexagonal.adapter.in.web.dto.OrderResponse;
import com.example.hexagonal.application.command.CreateOrderCommand;
import com.example.hexagonal.application.command.CreateOrderItemCommand;
import com.example.hexagonal.application.exception.OrderNotFoundException;
import com.example.hexagonal.application.port.in.CancelOrderUseCase;
import com.example.hexagonal.application.port.in.CreateOrderUseCase;
import com.example.hexagonal.application.port.in.GetOrderUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;

    public OrderController(
            CreateOrderUseCase createOrderUseCase,
            GetOrderUseCase getOrderUseCase,
            CancelOrderUseCase cancelOrderUseCase
    ) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        var command = new CreateOrderCommand(
                request.customerId(),
                request.items().stream()
                        .map(item -> new CreateOrderItemCommand(
                                item.productCode(),
                                item.quantity(),
                                item.unitPrice(),
                                item.currency()
                        ))
                        .toList()
        );

        return OrderResponse.from(createOrderUseCase.create(command));
    }

    @GetMapping("/{orderId}")
    public OrderResponse getById(@PathVariable String orderId) {
        return getOrderUseCase.findById(orderId)
                .map(OrderResponse::from)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    @PostMapping("/{orderId}/cancel")
    public OrderResponse cancel(@PathVariable String orderId) {
        return OrderResponse.from(cancelOrderUseCase.cancel(orderId));
    }
}

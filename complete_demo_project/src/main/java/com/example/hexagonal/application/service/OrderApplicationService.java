package com.example.hexagonal.application.service;

import com.example.hexagonal.application.command.CreateOrderCommand;
import com.example.hexagonal.application.exception.OrderNotFoundException;
import com.example.hexagonal.application.port.in.CancelOrderUseCase;
import com.example.hexagonal.application.port.in.CreateOrderUseCase;
import com.example.hexagonal.application.port.in.GetOrderUseCase;
import com.example.hexagonal.application.port.out.ClockPort;
import com.example.hexagonal.application.port.out.DomainEventPublisherPort;
import com.example.hexagonal.application.port.out.IdGeneratorPort;
import com.example.hexagonal.application.port.out.OrderRepositoryPort;
import com.example.hexagonal.application.query.OrderView;
import com.example.hexagonal.domain.model.CustomerId;
import com.example.hexagonal.domain.model.Money;
import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderId;
import com.example.hexagonal.domain.model.OrderItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderApplicationService implements CreateOrderUseCase, GetOrderUseCase, CancelOrderUseCase {

    private final OrderRepositoryPort repository;
    private final IdGeneratorPort idGenerator;
    private final ClockPort clock;
    private final DomainEventPublisherPort eventPublisher;

    public OrderApplicationService(
            OrderRepositoryPort repository,
            IdGeneratorPort idGenerator,
            ClockPort clock,
            DomainEventPublisherPort eventPublisher
    ) {
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.clock = clock;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public OrderView create(CreateOrderCommand command) {
        var items = command.items().stream()
                .map(item -> new OrderItem(
                        item.productCode(),
                        item.quantity(),
                        Money.of(item.unitPrice(), item.currency())
                ))
                .toList();

        var order = Order.create(
                new OrderId(idGenerator.nextId()),
                new CustomerId(command.customerId()),
                clock.now(),
                items
        );

        var events = order.drainEvents();
        var saved = repository.save(order);
        eventPublisher.publishAll(events);

        return toView(saved);
    }

    @Override
    public Optional<OrderView> findById(String orderId) {
        return repository.findById(new OrderId(orderId)).map(this::toView);
    }

    @Override
    @Transactional
    public OrderView cancel(String orderId) {
        var order = repository.findById(new OrderId(orderId))
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.cancel();

        var saved = repository.save(order);
        return toView(saved);
    }

    private OrderView toView(Order order) {
        var total = order.total();
        List<OrderView.OrderItemView> items = order.items().stream()
                .map(item -> new OrderView.OrderItemView(
                        item.productCode(),
                        item.quantity(),
                        item.unitPrice().amount(),
                        item.subtotal().amount(),
                        item.unitPrice().currency().getCurrencyCode()
                ))
                .toList();

        return new OrderView(
                order.id().value(),
                order.customerId().value(),
                order.status().name(),
                order.createdAt(),
                total.amount(),
                total.currency().getCurrencyCode(),
                order.version(),
                items
        );
    }
}

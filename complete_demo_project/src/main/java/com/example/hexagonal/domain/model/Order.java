package com.example.hexagonal.domain.model;

import com.example.hexagonal.domain.event.DomainEvent;
import com.example.hexagonal.domain.event.OrderCreatedEvent;
import com.example.hexagonal.domain.exception.BusinessRuleViolationException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private final OrderId id;
    private final CustomerId customerId;
    private final Instant createdAt;
    private final List<OrderItem> items;
    private final List<DomainEvent> pendingEvents;
    private OrderStatus status;
    private long version;

    private Order(
            OrderId id,
            CustomerId customerId,
            Instant createdAt,
            List<OrderItem> items,
            OrderStatus status,
            long version,
            List<DomainEvent> pendingEvents
    ) {
        this.id = Objects.requireNonNull(id, "orderId is required");
        this.customerId = Objects.requireNonNull(customerId, "customerId is required");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt is required");
        this.items = List.copyOf(items);
        this.status = Objects.requireNonNull(status, "status is required");
        this.version = version;
        this.pendingEvents = new ArrayList<>(pendingEvents);

        if (this.items.isEmpty()) {
            throw new IllegalArgumentException("order must contain at least one item");
        }
    }

    public static Order create(OrderId id, CustomerId customerId, Instant now, List<OrderItem> items) {
        var order = new Order(id, customerId, now, items, OrderStatus.CREATED, 0L, List.of());
        order.pendingEvents.add(new OrderCreatedEvent(order.id.value(), order.customerId.value(), now));
        return order;
    }

    public static Order restore(
            OrderId id,
            CustomerId customerId,
            Instant createdAt,
            List<OrderItem> items,
            OrderStatus status,
            long version
    ) {
        return new Order(id, customerId, createdAt, items, status, version, List.of());
    }

    public void cancel() {
        if (status == OrderStatus.CANCELLED) {
            throw new BusinessRuleViolationException("order is already cancelled");
        }
        status = OrderStatus.CANCELLED;
    }

    public Money total() {
        var firstCurrency = items.getFirst().unitPrice().currency().getCurrencyCode();
        var total = Money.of(java.math.BigDecimal.ZERO, firstCurrency);
        for (OrderItem item : items) {
            total = total.add(item.subtotal());
        }
        return total;
    }

    public List<DomainEvent> drainEvents() {
        var events = List.copyOf(pendingEvents);
        pendingEvents.clear();
        return events;
    }

    public OrderId id() {
        return id;
    }

    public CustomerId customerId() {
        return customerId;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public List<OrderItem> items() {
        return items;
    }

    public OrderStatus status() {
        return status;
    }

    public long version() {
        return version;
    }

    public void incrementVersion() {
        this.version++;
    }
}

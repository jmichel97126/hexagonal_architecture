package com.example.hexagonal.adapter.out.persistence;

import com.example.hexagonal.adapter.out.persistence.jpa.OrderItemJpaEntity;
import com.example.hexagonal.adapter.out.persistence.jpa.OrderJpaEntity;
import com.example.hexagonal.adapter.out.persistence.jpa.SpringDataOrderRepository;
import com.example.hexagonal.application.port.out.OrderRepositoryPort;
import com.example.hexagonal.domain.model.CustomerId;
import com.example.hexagonal.domain.model.Money;
import com.example.hexagonal.domain.model.Order;
import com.example.hexagonal.domain.model.OrderId;
import com.example.hexagonal.domain.model.OrderItem;
import com.example.hexagonal.domain.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderPersistenceAdapter implements OrderRepositoryPort {

    private final SpringDataOrderRepository repository;

    public OrderPersistenceAdapter(SpringDataOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order save(Order order) {
        var entity = repository.findById(order.id().value()).orElseGet(OrderJpaEntity::new);

        entity.setId(order.id().value());
        entity.setCustomerId(order.customerId().value());
        entity.setCreatedAt(order.createdAt());
        entity.setStatus(order.status().name());
        entity.setVersion(order.version());

        entity.clearItems();
        order.items().forEach(item -> entity.addItem(toItemEntity(item)));

        var saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return repository.findById(orderId.value()).map(this::toDomain);
    }

    private OrderItemJpaEntity toItemEntity(OrderItem item) {
        var entity = new OrderItemJpaEntity();
        entity.setProductCode(item.productCode());
        entity.setQuantity(item.quantity());
        entity.setUnitPrice(item.unitPrice().amount());
        entity.setCurrency(item.unitPrice().currency().getCurrencyCode());
        return entity;
    }

    private Order toDomain(OrderJpaEntity entity) {
        var items = entity.getItems().stream()
                .map(item -> new OrderItem(
                        item.getProductCode(),
                        item.getQuantity(),
                        Money.of(item.getUnitPrice(), item.getCurrency())
                ))
                .toList();

        return Order.restore(
                new OrderId(entity.getId()),
                new CustomerId(entity.getCustomerId()),
                entity.getCreatedAt(),
                items,
                OrderStatus.valueOf(entity.getStatus()),
                entity.getVersion()
        );
    }
}

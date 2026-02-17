package com.example.hexagonal.adapter.out.persistence;

import com.example.hexagonal.application.port.out.LoadOrderPort;
import com.example.hexagonal.application.port.out.SaveOrderPort;
import com.example.hexagonal.domain.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryOrderRepository implements SaveOrderPort, LoadOrderPort {

    private final Map<UUID, Order> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Order order) {
        storage.put(order.id(), order);
    }

    @Override
    public Optional<Order> loadById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }
}
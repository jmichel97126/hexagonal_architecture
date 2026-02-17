package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.event.DomainEvent;

import java.util.List;

public interface DomainEventPublisherPort {

    void publishAll(List<DomainEvent> events);
}

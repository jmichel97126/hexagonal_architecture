package com.example.hexagonal.adapter.out.event;

import com.example.hexagonal.application.port.out.DomainEventPublisherPort;
import com.example.hexagonal.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoggingDomainEventPublisherAdapter implements DomainEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingDomainEventPublisherAdapter.class);

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(event -> log.info("Domain event published: type={}, payload={}", event.type(), event));
    }
}

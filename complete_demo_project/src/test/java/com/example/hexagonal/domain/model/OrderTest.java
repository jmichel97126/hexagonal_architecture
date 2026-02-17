package com.example.hexagonal.domain.model;

import com.example.hexagonal.domain.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void should_compute_total_for_multiple_items() {
        var order = Order.create(
                new OrderId("o-1"),
                new CustomerId("c-1"),
                Instant.now(),
                List.of(
                        new OrderItem("P1", 2, Money.of(new BigDecimal("10.00"), "EUR")),
                        new OrderItem("P2", 1, Money.of(new BigDecimal("3.50"), "EUR"))
                )
        );

        assertEquals(new BigDecimal("23.50"), order.total().amount());
        assertEquals("EUR", order.total().currency().getCurrencyCode());
    }

    @Test
    void should_reject_double_cancel() {
        var order = Order.create(
                new OrderId("o-2"),
                new CustomerId("c-2"),
                Instant.now(),
                List.of(new OrderItem("P1", 1, Money.of(new BigDecimal("1.00"), "EUR")))
        );

        order.cancel();

        assertThrows(BusinessRuleViolationException.class, order::cancel);
    }
}

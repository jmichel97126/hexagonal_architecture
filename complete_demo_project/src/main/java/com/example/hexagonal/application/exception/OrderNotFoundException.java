package com.example.hexagonal.application.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("order not found: " + orderId);
    }
}

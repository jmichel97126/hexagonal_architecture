package com.example.hexagonal.application.port.out;

import com.example.hexagonal.domain.model.Order;

public interface SaveOrderPort {

    void save(Order order);
}
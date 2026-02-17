package com.example.hexagonal.adapter.out.system;

import com.example.hexagonal.application.port.out.IdGeneratorPort;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidGeneratorAdapter implements IdGeneratorPort {

    @Override
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}

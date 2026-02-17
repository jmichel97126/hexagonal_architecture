package com.example.hexagonal.application.port.out;

import java.time.Instant;

public interface ClockPort {

    Instant now();
}

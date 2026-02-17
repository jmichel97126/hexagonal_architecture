package com.example.hexagonal.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderJpaEntity, String> {
}

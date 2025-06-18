package com.harusari.chainware.order.command.domain.repository;

import com.harusari.chainware.order.command.domain.aggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
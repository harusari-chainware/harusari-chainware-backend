package com.harusari.chainware.order.command.domain.repository;

import com.harusari.chainware.order.command.domain.aggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
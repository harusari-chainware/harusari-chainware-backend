package com.harusari.chainware.order.query.repository;

import com.harusari.chainware.order.command.domain.aggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderQueryRepository extends JpaRepository<Order, Long>, OrderQueryRepositoryCustom {
}
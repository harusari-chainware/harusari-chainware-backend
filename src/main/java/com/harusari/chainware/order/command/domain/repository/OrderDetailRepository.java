package com.harusari.chainware.order.command.domain.repository;

import com.harusari.chainware.order.command.domain.aggregate.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    void deleteAllByOrderId(Long orderId);

}
package com.harusari.chainware.delivery.command.domain.repository;

import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    Optional<Delivery> findByOrderId(Long orderId);

}

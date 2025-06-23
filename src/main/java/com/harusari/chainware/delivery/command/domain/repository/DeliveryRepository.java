package com.harusari.chainware.delivery.command.domain.repository;

import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}

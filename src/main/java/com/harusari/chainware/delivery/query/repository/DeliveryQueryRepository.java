package com.harusari.chainware.delivery.query.repository;

import com.harusari.chainware.delivery.command.domain.aggregate.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryQueryRepository extends JpaRepository<Delivery, Long>, DeliveryQueryRepositoryCustom {
}

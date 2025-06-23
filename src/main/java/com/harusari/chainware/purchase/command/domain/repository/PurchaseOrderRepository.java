package com.harusari.chainware.purchase.command.domain.repository;

import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}

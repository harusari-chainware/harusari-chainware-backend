package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInbound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseInboundRepository extends JpaRepository<WarehouseInbound, Long> {
}
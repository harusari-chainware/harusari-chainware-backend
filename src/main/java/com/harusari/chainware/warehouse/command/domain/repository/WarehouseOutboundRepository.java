package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseOutbound;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseOutboundRepository extends JpaRepository<WarehouseOutbound, Long> {
}

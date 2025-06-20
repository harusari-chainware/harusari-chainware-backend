package com.harusari.chainware.warehouse.command.infrastructure.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventorySnapshot;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventorySnapshotRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWarehouseInventorySnapshotRepository extends WarehouseInventorySnapshotRepository, JpaRepository<WarehouseInventorySnapshot, Long> {
}
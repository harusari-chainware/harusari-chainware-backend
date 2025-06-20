package com.harusari.chainware.warehouse.command.domain.repository;

import java.time.LocalDate;

public interface WarehouseInventorySnapshotRepository {
    boolean existsByWarehouseIdAndProductIdAndSnapshotDate(Long warehouseId, Long productId, LocalDate snapshotDate);
}
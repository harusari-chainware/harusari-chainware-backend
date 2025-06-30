package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;

import java.util.Optional;

public interface WarehouseInventoryRepository {
    WarehouseInventory save(WarehouseInventory inventory);
    Optional<WarehouseInventory> findByProductId(Long productId);
    Optional<WarehouseInventory> findByWarehouseIdAndProductId(Long warehouseId, Long productId);

}
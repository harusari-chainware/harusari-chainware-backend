package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;

import java.util.Optional;

public interface WarehouseInventoryRepository {
    WarehouseInventory save(WarehouseInventory inventory);
}
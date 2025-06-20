package com.harusari.chainware.warehouse.command.infrastructure.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWarehouseInventoryRepository extends WarehouseInventoryRepository, JpaRepository<WarehouseInventory, Long> {

}
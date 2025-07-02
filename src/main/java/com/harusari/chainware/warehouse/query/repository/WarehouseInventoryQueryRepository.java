package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseInventoryQueryRepository extends JpaRepository<WarehouseInventory, Long>, WarehouseInventoryQueryRepositoryCustom {
}

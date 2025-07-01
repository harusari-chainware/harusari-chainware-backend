package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseQueryRepository extends JpaRepository<Warehouse, Long>, WarehouseQueryRepositoryCustom {
}

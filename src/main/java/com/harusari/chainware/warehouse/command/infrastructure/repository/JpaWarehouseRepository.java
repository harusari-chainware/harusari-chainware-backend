package com.harusari.chainware.warehouse.command.infrastructure.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWarehouseRepository extends WarehouseRepository, JpaRepository<Warehouse, Long> {

}
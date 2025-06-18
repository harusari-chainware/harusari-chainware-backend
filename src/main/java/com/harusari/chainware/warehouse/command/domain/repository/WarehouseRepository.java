package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;

public interface WarehouseRepository {

    Warehouse save(Warehouse warehouse);

}
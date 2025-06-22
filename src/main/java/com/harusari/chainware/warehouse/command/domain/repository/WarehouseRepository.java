package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;

import java.util.Optional;

public interface WarehouseRepository {

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findWarehouseIdByMemberId(Long memberId);

}
package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    Optional<Warehouse> findByMemberIdAndIsDeletedFalse(Long memberId);

    default Optional<Warehouse> findWarehouseIdByMemberId(Long memberId) {
        return findByMemberIdAndIsDeletedFalse(memberId);
    }

}
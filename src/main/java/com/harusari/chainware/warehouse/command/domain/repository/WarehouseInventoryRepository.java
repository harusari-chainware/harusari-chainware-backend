package com.harusari.chainware.warehouse.command.domain.repository;

import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Long> {

    Optional<WarehouseInventory> findByProductId(Long productId);

    // 비관적 락을 이용한 창고 재고 수량 검증
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM WarehouseInventory w WHERE w.productId = :productId")
    Optional<WarehouseInventory> findByProductIdForUpdate(@Param("productId") Long productId);
}
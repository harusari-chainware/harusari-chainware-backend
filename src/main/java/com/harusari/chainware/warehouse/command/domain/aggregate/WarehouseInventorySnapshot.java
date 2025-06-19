package com.harusari.chainware.warehouse.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_inventory_snapshot")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarehouseInventorySnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snapshot_id")
    private Long snapshotId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "snapshot_date")
    private LocalDate snapshotDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt;

    @Builder
    public WarehouseInventorySnapshot(Long warehouseId, Long productId, LocalDate snapshotDate, Integer quantity, LocalDateTime recordedAt) {
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.snapshotDate = snapshotDate;
        this.quantity = quantity;
        this.recordedAt = recordedAt;
    }
}

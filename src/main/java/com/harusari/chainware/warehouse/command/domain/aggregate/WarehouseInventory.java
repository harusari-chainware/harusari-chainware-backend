package com.harusari.chainware.warehouse.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "warehouse_inventory")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarehouseInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_inventory_id")
    private Long warehouseInventoryId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public WarehouseInventory(Long productId, Long warehouseId,
                              Integer quantity, Integer reservedQuantity) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.reservedQuantity = reservedQuantity;
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    public void updateQuantity(int newQuantity, LocalDateTime modifiedAt) {
        this.quantity = newQuantity;
        this.modifiedAt = modifiedAt;
    }

    public void decreaseQuantity(int delta, LocalDateTime modifiedAt) {
        this.quantity -= delta;
        this.modifiedAt = modifiedAt;
    }

    public void increaseReservedQuantity(int delta, LocalDateTime modifiedAt) {
        this.reservedQuantity += delta;
        this.modifiedAt = modifiedAt;
    }

    public void decreaseReservedQuantity(int delta, LocalDateTime modifiedAt) {
        this.reservedQuantity -= delta;
        this.modifiedAt = modifiedAt;
    }

}

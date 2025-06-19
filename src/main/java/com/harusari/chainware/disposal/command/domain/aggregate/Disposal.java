package com.harusari.chainware.disposal.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "disposal")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Disposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disposal_id")
    private Long disposalId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "franchise_id")
    private Long franchiseId;

    @Column(name = "take_back_id")
    private Long takeBackId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "disposal_reason")
    private String disposalReason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Disposal(Long productId, Long warehouseId, Long franchiseId, Long takeBackId, Integer quantity, String disposalReason, LocalDateTime createdAt) {
        this.productId = productId;
        this.warehouseId = warehouseId;
        this.franchiseId = franchiseId;
        this.takeBackId = takeBackId;
        this.quantity = quantity;
        this.disposalReason = disposalReason;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}

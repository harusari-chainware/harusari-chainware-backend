package com.harusari.chainware.warehouse.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_inbound")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarehouseInbound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inbound_id")
    private Long inboundId;

    private Long purchaseOrderId;

    private Long warehouseId;

    private Long productId;

    private int unitQuantity;

    private LocalDate expirationDate;

    private LocalDateTime inboundedAt;

    @Builder
    public WarehouseInbound(Long inboundId, Long purchaseOrderId, Long warehouseId,
                            Long productId, int unitQuantity, LocalDate expirationDate, LocalDateTime inboundedAt
                         ) {
        this.inboundId = inboundId;
        this.purchaseOrderId = purchaseOrderId;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.unitQuantity = unitQuantity;
        this.expirationDate = expirationDate;
        this.inboundedAt = inboundedAt;
    }
}

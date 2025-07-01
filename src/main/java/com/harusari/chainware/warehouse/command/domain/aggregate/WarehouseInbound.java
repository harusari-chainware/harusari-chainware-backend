package com.harusari.chainware.warehouse.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "purchase_order_id")
    private Long purchaseOrderId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "unit_quantity")
    private int unitQuantity;

    @Column(name = "inbounded_at")
    private LocalDateTime inboundedAt;

    @Builder
    public WarehouseInbound(Long inboundId, Long purchaseOrderId, Long warehouseId,
                            Long productId, int unitQuantity, LocalDateTime inboundedAt
                         ) {
        this.inboundId = inboundId;
        this.purchaseOrderId = purchaseOrderId;
        this.warehouseId = warehouseId;
        this.productId = productId;
        this.unitQuantity = unitQuantity;
        this.inboundedAt = inboundedAt;
    }
}

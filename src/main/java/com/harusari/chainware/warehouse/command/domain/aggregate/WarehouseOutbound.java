package com.harusari.chainware.warehouse.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "warehouse_outbound")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarehouseOutbound {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outbound_id")
    private Long outboundId;

    @Column(name = "store_order_id")
    private Long orderId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "delivery_id")
    private Long deliveryId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "expiraion_date")
    private LocalDate expiraionDate;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "outbounded_at")
    private LocalDateTime outboundedAt;

    @Builder
    public WarehouseOutbound(
            Long orderId, Long warehouseId, Long deliveryId,
            Long productId, LocalDate expiraionDate, Integer quantity
    ) {
        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.deliveryId = deliveryId;
        this.productId = productId;
        this.expiraionDate = expiraionDate;
        this.quantity = quantity;
        this.outboundedAt = LocalDateTime.now().withNano(0);
    }

}

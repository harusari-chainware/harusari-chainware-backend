package com.harusari.chainware.requisition.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "requisition_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RequisitionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requisitionDetailId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requisition_id", nullable = false)
    private Requisition requisition;

    @Column(nullable = false)
    private Long contractId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(nullable = false, precision = 20, scale = 2)
    private BigDecimal totalPrice;

    public static RequisitionDetail create(Long contractId, Long productId, int quantity, BigDecimal unitPrice) {
        return RequisitionDetail.builder()
                .contractId(contractId)
                .productId(productId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalPrice(unitPrice.multiply(BigDecimal.valueOf(quantity)))
                .build();
    }
}

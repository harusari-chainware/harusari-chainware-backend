package com.harusari.chainware.requisition.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;


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

    @Column(nullable = false)
    private Long unitPrice;

    @Column(nullable = false)
    private Long totalPrice;

    public static RequisitionDetail create(Long contractId, Long productId, int quantity, Long unitPrice) {
        return RequisitionDetail.builder()
                .contractId(contractId)
                .productId(productId)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalPrice(unitPrice * quantity)
                .build();
    }
}

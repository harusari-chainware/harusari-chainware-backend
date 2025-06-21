package com.harusari.chainware.requisition.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "requisition_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RequisitionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requisitionDetailId;

    @Column(name = "requisition_id", nullable = false)
    private Long requisitionId;

    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private Long unitPrice;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Builder
    public RequisitionDetail(Long requisitionId, Long contractId, Long productId, int quantity, Long unitPrice) {
        this.requisitionId = requisitionId;
        this.contractId = contractId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = unitPrice * quantity;
    }
}
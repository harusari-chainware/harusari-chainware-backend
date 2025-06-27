package com.harusari.chainware.requisition.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "requisition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Requisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requisitionId;

    @Column(name = "created_member_id", nullable = false)
    private Long createdMemberId;

    @Column(name = "approved_member_id", nullable = false)
    private Long approvedMemberId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "requisition_code", nullable = false, length = 50)
    private String requisitionCode;

    @Column(name = "product_count", nullable = false)
    private int productCount;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "requisition_status", nullable = false)
    private RequisitionStatus requisitionStatus;

    @Column(name = "reject_reason", length = 255)
    private String rejectReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public Requisition(Long createdMemberId, Long approvedMemberId, Long vendorId, Long warehouseId, String code,
                       int productCount, int totalQuantity, Long totalPrice) {
        this.createdMemberId = createdMemberId;
        this.approvedMemberId = approvedMemberId;
        this.vendorId = vendorId;
        this.warehouseId = warehouseId;
        this.requisitionCode = code;
        this.productCount = productCount;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.requisitionStatus = RequisitionStatus.SAVED;
        this.createdAt = LocalDateTime.now().withNano(0);
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    public void submit() {
        this.requisitionStatus = RequisitionStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now().withNano(0);
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    public void approve(Long approvedMemberId) {
        this.requisitionStatus = RequisitionStatus.APPROVED;
        this.approvedMemberId = approvedMemberId;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }

    public void reject(String rejectReason) {
        this.requisitionStatus = RequisitionStatus.REJECTED;
        this.rejectReason = rejectReason;
        this.modifiedAt = LocalDateTime.now().withNano(0);
    }


    public void update(Long approvedMemberId, Long warehouseId, int productCount, int totalQuantity, Long totalPrice) {
        this.approvedMemberId = approvedMemberId;
        this.warehouseId = warehouseId;
        this.productCount = productCount;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.modifiedAt = LocalDateTime.now();
    }

    public boolean isWriter(Long memberId) {
        return this.createdMemberId.equals(memberId);
    }

    public boolean isModifiable() {
        return this.requisitionStatus != RequisitionStatus.SUBMITTED &&
                this.requisitionStatus != RequisitionStatus.APPROVED &&
                this.requisitionStatus != RequisitionStatus.REJECTED;
    }
}

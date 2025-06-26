package com.harusari.chainware.purchase.command.domain.aggregate;

import com.harusari.chainware.exception.purchase.PurchaseOrderException;
import com.harusari.chainware.requisition.command.application.exception.InvalidStatusException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchase_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderId;

    @Column(name = "requisition_id", nullable = false)
    private Long requisitionId;

    @Column(name = "vendor_id", nullable = false)
    private Long vendorId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "created_member_id", nullable = false)
    private Long createdMemberId;

    @Column(name = "vendor_member_id", nullable = false)
    private Long vendorMemberId;

    @Column(name = "purchase_order_code", length = 50, nullable = false)
    private String purchaseOrderCode;

    @Column(name = "total_amount", nullable = false)
    private Long totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "purchase_order_status", nullable = false, length = 20)
    private PurchaseOrderStatus purchaseOrderStatus;

    @Column(name = "reject_reason", length = 255)
    private String rejectReason;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Builder
    public PurchaseOrder(Long requisitionId, Long vendorId, Long warehouseId, Long createdMemberId, Long vendorMemberId,
                         String purchaseOrderCode, Long totalAmount, PurchaseOrderStatus purchaseOrderStatus,
                         String rejectReason, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.requisitionId = requisitionId;
        this.vendorId = vendorId;
        this.warehouseId = warehouseId;
        this.createdMemberId = createdMemberId;
        this.vendorMemberId = vendorMemberId;
        this.purchaseOrderCode = purchaseOrderCode;
        this.totalAmount = totalAmount;
        this.purchaseOrderStatus = purchaseOrderStatus;
        this.rejectReason = rejectReason;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }


    // 발주 승인
    public void approve() {
        this.purchaseOrderStatus = PurchaseOrderStatus.APPROVED;
        this.modifiedAt = LocalDateTime.now();
    }

    // 발주 거절
    public void reject(String rejectReason) {
        this.purchaseOrderStatus = PurchaseOrderStatus.REJECTED;
        this.rejectReason = rejectReason;
        this.modifiedAt = LocalDateTime.now();
    }

    // 발주 요청 취소
    public void cancel(String cancelReason) {
        this.purchaseOrderStatus = PurchaseOrderStatus.CANCELLED;
        this.rejectReason = cancelReason; // 사유 저장 재활용
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
        this.modifiedAt = LocalDateTime.now();
    }

    // 발주 출고
    public void shipped() {
        this.purchaseOrderStatus = PurchaseOrderStatus.SHIPPED;
        this.modifiedAt = LocalDateTime.now();
    }
}

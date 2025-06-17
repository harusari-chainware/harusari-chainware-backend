package com.harusari.chainware.requisition.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requisition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

    @Column(name = "requisition_code", nullable = false, length = 50)
    private String requisitionCode;

    @Column(name = "product_count", nullable = false)
    private int productCount;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

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

    @Builder.Default
    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionDetail> details = new ArrayList<>();

    public void addDetails(List<RequisitionDetail> detailList) {
        this.details.clear();
        for (RequisitionDetail detail : detailList) {
            detail.setRequisition(this);
            this.details.add(detail);
        }
    }

    public static Requisition create(Long createdMemberId, Long approvedMemberId, Long vendorId, String code,
                                     int productCount, int totalQuantity, BigDecimal totalPrice) {
        return Requisition.builder()
                .createdMemberId(createdMemberId)
                .approvedMemberId(approvedMemberId)
                .vendorId(vendorId)
                .requisitionCode(code)
                .productCount(productCount)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .requisitionStatus(RequisitionStatus.SAVED)
                .modifiedAt(LocalDateTime.now().withNano(0))
                .build();
    }

    public void submit() {
        this.requisitionStatus = RequisitionStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }
}

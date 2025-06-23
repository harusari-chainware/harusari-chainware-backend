package com.harusari.chainware.requisition.query.dto.response;

import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RequisitionDetailResponse {
    private Long requisitionId;
    private String requisitionCode;
    private RequisitionStatus requisitionStatus;
    private Long createdMemberId;
    private Long approvedMemberId;
    private Long vendorId;
    private int productCount;
    private int totalQuantity;
    private Long totalPrice;
    private String rejectReason;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime modifiedAt;
    private List<RequisitionItemResponse> items;
}

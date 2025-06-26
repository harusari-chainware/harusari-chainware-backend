package com.harusari.chainware.requisition.query.dto.response;

import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RequisitionSummaryResponse {
    private Long requisitionId;
    private String requisitionCode;
    private RequisitionStatus requisitionStatus;
    private Long vendorId;
    private Long warehouseId;
    private int productCount;
    private int totalQuantity;
    private Long totalPrice;
    private LocalDateTime createdAt;
}

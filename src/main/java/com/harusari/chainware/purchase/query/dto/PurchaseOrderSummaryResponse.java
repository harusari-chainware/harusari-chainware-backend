package com.harusari.chainware.purchase.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;



@Getter
@Builder
public class PurchaseOrderSummaryResponse {
    private Long purchaseOrderId;
    private String purchaseOrderCode;
    private String status;
    private Long vendorId;
    private Long warehouseId;
    private Long requesterId;
    private LocalDateTime createdAt;
    private Long totalAmount;
}

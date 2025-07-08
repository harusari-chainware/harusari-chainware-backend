package com.harusari.chainware.purchase.query.dto;

import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Getter
@Builder
public class PurchaseOrderSummaryResponse {
    private Long purchaseOrderId;
    private String purchaseOrderCode;
    private PurchaseOrderStatus status;

    private String vendorName;

    private int productCount;
    private int totalQuantity;
    private Long totalAmount;

    private String requisitionCode;

    private LocalDate createdAt;
    private LocalDate dueDate;
    private LocalDate shippedAt;
}

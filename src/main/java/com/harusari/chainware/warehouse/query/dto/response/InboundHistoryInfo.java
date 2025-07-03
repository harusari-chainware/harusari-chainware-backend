package com.harusari.chainware.warehouse.query.dto.response;

import com.harusari.chainware.purchase.command.domain.aggregate.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InboundHistoryInfo {
    private String vendorName;
    private String createdBy;
    private String vendorContact;
    private LocalDateTime createdAt;
    private PurchaseOrderStatus purchaseOrderStatus;
    private Long requisitionId;
    private String requisitionCode;
    private int productCount;
    private int totalQuantity;
    private long totalPrice;
}
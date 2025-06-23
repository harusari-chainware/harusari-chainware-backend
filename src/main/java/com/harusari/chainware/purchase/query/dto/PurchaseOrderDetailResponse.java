package com.harusari.chainware.purchase.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PurchaseOrderDetailResponse {

    private Long purchaseOrderId;
    private String purchaseOrderCode;
    private String vendorName;
    private String status;
    private Long totalAmount;
    private LocalDateTime createdAt;
    private List<PurchaseOrderProductResponse> products;
}
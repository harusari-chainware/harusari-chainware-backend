package com.harusari.chainware.purchase.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class PurchaseOrderInfo {
    private Long purchaseOrderId;
    private String purchaseOrderCode;
    private String requisitionCode; // 관련 품의 코드
    private String status;
    private Long totalAmount;

    private LocalDateTime createdAt;
    private LocalDateTime approvedAt;
    private LocalDateTime submittedAt;
    private LocalDateTime shippedAt;
    private LocalDate dueDate;
}

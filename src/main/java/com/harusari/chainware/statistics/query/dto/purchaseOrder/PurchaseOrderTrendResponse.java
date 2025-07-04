package com.harusari.chainware.statistics.query.dto.purchaseOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PurchaseOrderTrendResponse {
    private LocalDate date;
    private long totalQuantity;
    private long totalAmount;
}
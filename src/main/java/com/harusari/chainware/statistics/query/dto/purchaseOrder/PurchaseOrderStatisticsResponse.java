package com.harusari.chainware.statistics.query.dto.purchaseOrder;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PurchaseOrderStatisticsResponse implements PurchaseOrderStatisticsResponseBase {
    private LocalDate date;
    private String vendorName;
    private int totalQuantity;
    private int totalOrders;
    private long totalAmount;
}
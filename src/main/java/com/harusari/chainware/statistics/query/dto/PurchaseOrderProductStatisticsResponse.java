package com.harusari.chainware.statistics.query.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PurchaseOrderProductStatisticsResponse implements PurchaseOrderStatisticsResponseBase {
    private LocalDate date;
    private String vendorName;
    private String productName;
    private int quantity;
    private long amount;
}
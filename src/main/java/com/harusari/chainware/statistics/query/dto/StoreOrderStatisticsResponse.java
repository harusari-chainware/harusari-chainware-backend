package com.harusari.chainware.statistics.query.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StoreOrderStatisticsResponse implements StoreOrderStatisticsResponseBase {
    private LocalDate date;
    private String franchiseName;
    private int totalQuantity;
    private int totalOrders;
    private long totalAmount;
}

package com.harusari.chainware.statistics.query.dto.storeOrder;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class StoreOrderProductStatisticsResponse implements StoreOrderStatisticsResponseBase {
    private LocalDate date;
    private String franchiseName;
    private String productName;
    private int quantity;
    private long amount;
}

package com.harusari.chainware.statistics.query.dto.salePattern;

import lombok.Getter;

@Getter
public class HourlySalesResponse {
    private final int hour;
    private final long totalAmount;
    private final boolean max;

    public HourlySalesResponse(int hour, long totalAmount) {
        this(hour, totalAmount, false);
    }

    public HourlySalesResponse(int hour, long totalAmount, boolean max) {
        this.hour = hour;
        this.totalAmount = totalAmount;
        this.max = max;
    }
}

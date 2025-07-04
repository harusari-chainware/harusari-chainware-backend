package com.harusari.chainware.statistics.query.dto.salePattern;

import lombok.Getter;

@Getter
public class WeekdaySalesResponse {

    private final String weekday;
    private final long dayCount;
    private final long totalAmount;
    private final long avgAmount;
    private final boolean max;

    public WeekdaySalesResponse(String weekday, long dayCount, long totalAmount, long avgAmount) {
        this(weekday, dayCount, totalAmount, avgAmount, false);
    }

    public WeekdaySalesResponse(String weekday, long dayCount, long totalAmount, long avgAmount, boolean max) {
        this.weekday = weekday;
        this.dayCount = dayCount;
        this.totalAmount = totalAmount;
        this.avgAmount = avgAmount;
        this.max = max;
    }
}

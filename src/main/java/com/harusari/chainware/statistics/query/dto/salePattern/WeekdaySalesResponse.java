package com.harusari.chainware.statistics.query.dto.salePattern;

import lombok.Getter;

@Getter
public class WeekdaySalesResponse {

    private String weekday;
    private long dayCount;
    private long totalAmount;
    private long avgAmount;
}

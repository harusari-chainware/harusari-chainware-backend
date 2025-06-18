package com.harusari.chainware.statistics.query.dto;

import lombok.Getter;

@Getter
public class HourlySalesResponse {
    private int hour;
    private long totalAmount;
}

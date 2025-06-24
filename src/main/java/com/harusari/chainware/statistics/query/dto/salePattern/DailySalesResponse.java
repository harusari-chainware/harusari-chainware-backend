package com.harusari.chainware.statistics.query.dto.salePattern;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DailySalesResponse {

    private LocalDate date;
    private Long totalAmount;
}

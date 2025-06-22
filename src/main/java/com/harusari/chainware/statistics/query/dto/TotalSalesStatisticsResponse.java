package com.harusari.chainware.statistics.query.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TotalSalesStatisticsResponse {
    private LocalDate date;
    private String franchiseName;
    private long totalSalesAmount;
    private double changeRate;
}
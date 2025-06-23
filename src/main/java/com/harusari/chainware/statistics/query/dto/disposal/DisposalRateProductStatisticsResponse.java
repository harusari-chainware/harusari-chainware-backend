package com.harusari.chainware.statistics.query.dto.disposal;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DisposalRateProductStatisticsResponse implements DisposalRateStatisticsResponseBase {
    private LocalDate date;
    private String productName;
    private int disposalQty;
    private int totalQty;
    private double disposalRate;
}
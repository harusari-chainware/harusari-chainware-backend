package com.harusari.chainware.statistics.query.dto.predictionComparison;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PredictionComparisonDto {
    private LocalDate date;
    private Long value;
    private String type; // "ACTUAL" or "PREDICTED"
}

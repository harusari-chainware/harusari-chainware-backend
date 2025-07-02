package com.harusari.chainware.statistics.query.dto.predictionAccuracy;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PredictionAccuracyResponseDto {

    private String predictionType;
    private LocalDate targetDate;
    private Double actualTotal;
    private Double predictedTotal;
    private Double mae;
    private Double rmse;
    private Double mape;
    private String franchiseName;

    public PredictionAccuracyResponseDto withMetrics(double mae, double rmse, Double mape) {
        return new PredictionAccuracyResponseDto(
                predictionType, targetDate,
                actualTotal, predictedTotal,
                mae, rmse, mape, franchiseName
        );
    }
}

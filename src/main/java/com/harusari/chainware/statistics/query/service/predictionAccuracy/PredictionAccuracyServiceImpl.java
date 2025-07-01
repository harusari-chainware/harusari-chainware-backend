package com.harusari.chainware.statistics.query.service.predictionAccuracy;

import com.harusari.chainware.statistics.query.dto.predictionAccuracy.PredictionAccuracyResponseDto;
import com.harusari.chainware.statistics.query.mapper.PredictionAccuracyQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredictionAccuracyServiceImpl implements PredictionAccuracyService {

    private final PredictionAccuracyQueryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Object getSummary(String predictionType, String periodType, LocalDate targetDate, Long franchiseId) {
        return switch (periodType.toUpperCase()) {
            case "DAILY" -> getDailySummary(predictionType, targetDate, franchiseId);
            case "WEEKLY" -> getRangeSummary(predictionType, targetDate.minusDays(6), targetDate, franchiseId);
            case "MONTHLY" -> getRangeSummary(predictionType, targetDate.minusDays(29), targetDate, franchiseId);
            default -> throw new IllegalArgumentException("Invalid periodType: " + periodType);
        };
    }

    private PredictionAccuracyResponseDto getDailySummary(String predictionType, LocalDate targetDate, Long franchiseId) {
        PredictionAccuracyResponseDto dto = (franchiseId == null)
                ? mapper.findDailyAccuracyByType(predictionType, targetDate)
                : mapper.findDailyAccuracyByFranchise(predictionType, targetDate, franchiseId);

        return calculateMetrics(dto);
    }

    private List<PredictionAccuracyResponseDto> getRangeSummary(String predictionType, LocalDate startDate, LocalDate endDate, Long franchiseId) {
        List<PredictionAccuracyResponseDto> dtos = (franchiseId == null)
                ? mapper.findWeeklyAccuracyByType(predictionType, startDate, endDate)
                : mapper.findWeeklyAccuracyByFranchise(predictionType, startDate, endDate, franchiseId);

        return dtos.stream().map(this::calculateMetrics).collect(Collectors.toList());
    }

    private PredictionAccuracyResponseDto calculateMetrics(PredictionAccuracyResponseDto dto) {
        if (dto != null && dto.getActualTotal() != null && dto.getPredictedTotal() != null) {
            double actual = dto.getActualTotal();
            double predicted = dto.getPredictedTotal();
            double mae = Math.abs(actual - predicted);
            double rmse = Math.sqrt(mae * mae);
            Double mape = (actual == 0) ? null : Math.abs((actual - predicted) / actual) * 100;
            return dto.withMetrics(mae, rmse, mape);
        }
        return dto;
    }
}


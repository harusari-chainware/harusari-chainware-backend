package com.harusari.chainware.statistics.query.service.predictionComparison;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.predictionComparison.PredictionComparisonDto;
import com.harusari.chainware.statistics.query.mapper.PredictionComparisonQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionComparisonQueryServiceImpl implements PredictionComparisonQueryService {

    private final PredictionComparisonQueryMapper mapper;

    @Override
    @Transactional
    public List<PredictionComparisonDto> getPredictionComparison(String predictionType, Long franchiseId) {
        LocalDate today = LocalDate.now();

        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);
        LocalDate thisSunday = thisMonday.plusDays(6);

        LocalDate lastMonday = thisMonday.minusWeeks(1);
        LocalDate lastSunday = lastMonday.plusDays(6);

        List<PredictionComparisonDto> result = new ArrayList<>();

        switch (predictionType) {
            case "sales" -> {
                result.addAll(mapper.getActualSales(lastMonday, lastSunday, franchiseId));
                result.addAll(mapper.getPredictedSales(thisMonday, thisSunday, franchiseId));
            }
            case "order_quantity" -> {
                result.addAll(mapper.getActualOrderQuantity(lastMonday, lastSunday, franchiseId));
                result.addAll(mapper.getPredictedOrderQuantity(thisMonday, thisSunday, franchiseId));
            }
            case "purchase_quantity" -> {
                result.addAll(mapper.getActualPurchaseQuantity(lastMonday, lastSunday));
                result.addAll(mapper.getPredictedPurchaseQuantity(thisMonday, thisSunday));
            }
            default -> throw new StatisticsException(StatisticsErrorCode.INVALID_PREDICTION_TYPE);
        }

        return result;
    }
}

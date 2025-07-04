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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<PredictionComparisonDto> actuals;
        List<PredictionComparisonDto> predictions;

        switch (predictionType) {
            case "sales" -> {
                actuals = (franchiseId != null)
                        ? mapper.getActualSales(lastMonday, lastSunday, franchiseId)
                        : mapper.getActualSalesAllFranchises(lastMonday, lastSunday);

                predictions = (franchiseId != null)
                        ? mapper.getPredictedSales(thisMonday, thisSunday, franchiseId)
                        : mapper.getPredictedSalesAllFranchises(thisMonday, thisSunday);
            }
            case "order_quantity" -> {
                actuals = (franchiseId != null)
                        ? mapper.getActualOrderQuantity(lastMonday, lastSunday, franchiseId)
                        : mapper.getActualOrderQuantityAllFranchises(lastMonday, lastSunday);

                predictions = (franchiseId != null)
                        ? mapper.getPredictedOrderQuantity(thisMonday, thisSunday, franchiseId)
                        : mapper.getPredictedOrderQuantityAllFranchises(thisMonday, thisSunday);
            }
            case "purchase_quantity" -> {
                actuals = mapper.getActualPurchaseQuantity(lastMonday, lastSunday);
                predictions = mapper.getPredictedPurchaseQuantity(thisMonday, thisSunday);
            }
            default -> throw new StatisticsException(StatisticsErrorCode.INVALID_PREDICTION_TYPE);
        }

        // 오늘 이전 날짜는 실제값으로 덮어쓰기
        Map<LocalDate, PredictionComparisonDto> predictionMap = new HashMap<>();
        for (PredictionComparisonDto dto : predictions) {
            predictionMap.put(dto.getDate(), dto);
        }

        for (PredictionComparisonDto actual : actuals) {
            if (!actual.getDate().isAfter(today.minusDays(1))) {
                predictionMap.put(actual.getDate(), actual);
            }
        }

        return new ArrayList<>(predictionMap.values());
    }
}

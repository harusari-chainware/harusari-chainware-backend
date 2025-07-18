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
import java.util.*;

@Service
@RequiredArgsConstructor
public class PredictionComparisonQueryServiceImpl implements PredictionComparisonQueryService {

    private final PredictionComparisonQueryMapper mapper;

    @Override
    @Transactional
    public List<PredictionComparisonDto> getPredictionComparison(String predictionType, Long franchiseId) {
        LocalDate today = LocalDate.now();

        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);      // 이번 주 월요일 (예: 2025-07-14)
        LocalDate thisSunday = thisMonday.plusDays(6);            // 이번 주 일요일 (예: 2025-07-20)
        LocalDate lastMonday = thisMonday.minusWeeks(1);          // 지난 주 월요일 (예: 2025-07-07)

        // 실적 조회 범위: 지난 주 월요일 ~ 오늘 이전 날짜까지 포함
        LocalDate actualStartDate = lastMonday;
        LocalDate actualEndDateExclusive = today;  // → XML에서 < #{endDate}로 비교하므로 오늘 제외

        List<PredictionComparisonDto> actuals;
        List<PredictionComparisonDto> predictions;

        switch (predictionType) {
            case "sales" -> {
                actuals = (franchiseId != null)
                        ? mapper.getActualSales(actualStartDate, actualEndDateExclusive, franchiseId)
                        : mapper.getActualSalesAllFranchises(actualStartDate, actualEndDateExclusive);

                predictions = (franchiseId != null)
                        ? mapper.getPredictedSales(thisMonday, thisSunday, franchiseId)
                        : mapper.getPredictedSalesAllFranchises(thisMonday, thisSunday);
            }
            case "order_quantity" -> {
                actuals = (franchiseId != null)
                        ? mapper.getActualOrderQuantity(actualStartDate, actualEndDateExclusive, franchiseId)
                        : mapper.getActualOrderQuantityAllFranchises(actualStartDate, actualEndDateExclusive);

                predictions = (franchiseId != null)
                        ? mapper.getPredictedOrderQuantity(thisMonday, thisSunday, franchiseId)
                        : mapper.getPredictedOrderQuantityAllFranchises(thisMonday, thisSunday);
            }
            case "purchase_quantity" -> {
                actuals = mapper.getActualPurchaseQuantity(actualStartDate, actualEndDateExclusive);
                predictions = mapper.getPredictedPurchaseQuantity(thisMonday, thisSunday);
            }
            default -> throw new StatisticsException(StatisticsErrorCode.INVALID_PREDICTION_TYPE);
        }

        // 예측값을 먼저 Map에 넣음
        Map<LocalDate, PredictionComparisonDto> predictionMap = new HashMap<>();
        for (PredictionComparisonDto predicted : predictions) {
            predictionMap.put(predicted.getDate(), predicted);
        }

        // 오늘 이전 날짜의 실적이 있다면 예측을 덮어씀
        for (PredictionComparisonDto actual : actuals) {
            LocalDate date = actual.getDate();
            if (!date.isAfter(today.minusDays(1))) {
                predictionMap.put(date, actual);
            }
        }

        // 날짜 오름차순 정렬 후 반환
        List<PredictionComparisonDto> result = new ArrayList<>(predictionMap.values());
        result.sort(Comparator.comparing(PredictionComparisonDto::getDate));
        return result;
    }
}
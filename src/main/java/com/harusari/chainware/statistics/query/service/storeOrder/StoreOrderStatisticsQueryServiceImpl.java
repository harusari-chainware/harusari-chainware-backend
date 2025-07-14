package com.harusari.chainware.statistics.query.service.storeOrder;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderTrendResponse;
import com.harusari.chainware.statistics.query.mapper.StoreOrderStatisticsQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreOrderStatisticsQueryServiceImpl implements StoreOrderStatisticsQueryService {

    private final StoreOrderStatisticsQueryMapper mapper;

    @Override
    @Transactional
    public List<? extends StoreOrderStatisticsResponseBase> getStatistics(
            String period, Long franchiseId, LocalDate targetDate, boolean includeProduct) {

        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);
        LocalDate startDate, endDate;

        switch (period.toUpperCase()) {
            case "DAILY" -> startDate = endDate = baseDate;
            case "WEEKLY" -> {
                endDate = baseDate;
                startDate = baseDate.minusDays(6);
            }
            case "MONTHLY" -> {
                endDate = baseDate;
                startDate = baseDate.minusDays(29);
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        }

        LocalDate today = LocalDate.now();
        if (today.isBefore(endDate)) {
            throw new StatisticsException(StatisticsErrorCode.PERIOD_NOT_COMPLETED);
        }

        return includeProduct
                ? mapper.getProductLevelStatistics(franchiseId, startDate, endDate)
                : mapper.getFranchiseLevelStatistics(franchiseId, startDate, endDate);
    }

    @Override
    @Transactional
    public List<StoreOrderTrendResponse> getTrend(String period, Long franchiseId, LocalDate targetDate) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);
        LocalDate startDate, endDate;

        switch (period.toUpperCase()) {
            case "DAILY" -> {
                endDate = baseDate;
                startDate = baseDate.minusDays(6); // 최근 7일
            }
            case "WEEKLY" -> {
                endDate = baseDate;
                startDate = baseDate.minusWeeks(6); // 최근 7주
            }
            case "MONTHLY" -> {
                endDate = baseDate;
                startDate = baseDate.minusMonths(6); // 최근 7달
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        }

        return mapper.getStoreOrderTrend(period.toUpperCase(), franchiseId, startDate, endDate);
    }

}

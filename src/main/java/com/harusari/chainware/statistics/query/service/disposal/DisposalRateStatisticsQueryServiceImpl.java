package com.harusari.chainware.statistics.query.service.disposal;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponse;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateTrendGroupedResponse;
import com.harusari.chainware.statistics.query.mapper.DisposalRateStatisticsQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisposalRateStatisticsQueryServiceImpl implements DisposalRateStatisticsQueryService {

    private final DisposalRateStatisticsQueryMapper mapper;

    @Override
    @Transactional
    public List<? extends DisposalRateStatisticsResponseBase> getDisposalStatistics(
            String period, Long warehouseId, Long franchiseId, LocalDate targetDate, boolean includeProduct) {

        LocalDate baseDate = targetDate != null ? targetDate : LocalDate.now().minusDays(1);
        LocalDate startDate;
        LocalDate endDate;

        switch (period) {
            case "DAILY" -> {
                startDate = baseDate.minusDays(6); // 최근 7일치
                endDate = baseDate;
            }
            case "WEEKLY" -> {
                startDate = baseDate.minusDays(6);
                endDate = baseDate;
            }

            case "MONTHLY" -> {
                startDate = baseDate.withDayOfMonth(1);
                endDate = baseDate;
            }

            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        }

        if (includeProduct) {
            return mapper.getProductLevelDisposalRate(
                    period, startDate, endDate, warehouseId, franchiseId
            );
        } else {
            return mapper.getDisposalRate(startDate, endDate, warehouseId, franchiseId);
        }
    }

    @Override
    @Transactional
    public DisposalRateTrendGroupedResponse getGroupedTrend(String period, LocalDate targetDate) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now();

        LocalDate startDate;
        LocalDate endDate;

        switch (period) {
            case "DAILY" -> {
                endDate = baseDate;
                startDate = baseDate.minusDays(6); // 7일
            }
            case "WEEKLY" -> {
                endDate = baseDate;
                startDate = baseDate.minusWeeks(6).with(DayOfWeek.MONDAY); // 7주, 주 시작일
            }
            case "MONTHLY" -> {
                endDate = baseDate;
                startDate = baseDate.minusMonths(6).withDayOfMonth(1); // 7개월
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        }

        return DisposalRateTrendGroupedResponse.builder()
                .total(mapper.getTrendForTotal(period, startDate, endDate))
                .headquarters(mapper.getTrendForHeadquarters(period, startDate, endDate))
                .franchises(mapper.getTrendForFranchises(period, startDate, endDate))
                .build();
    }

    @Override
    @Transactional
    public List<DisposalRateStatisticsResponse> getSingleTrend(
            String period,
            Long warehouseId,
            Long franchiseId,
            LocalDate targetDate) {

        LocalDate baseDate = targetDate != null ? targetDate : LocalDate.now().minusDays(1);
        LocalDate startDate;
        LocalDate endDate;

        switch (period) {
            case "DAILY" -> {
                startDate = baseDate.minusDays(6);
                endDate = baseDate;
            }
            case "WEEKLY" -> {
                endDate = baseDate;
                startDate = endDate.minusWeeks(6); // 총 7주
            }
            case "MONTHLY" -> {
                endDate = baseDate.withDayOfMonth(1);
                startDate = endDate.minusMonths(6); // 총 7개월
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        }

        return mapper.getTrendForSingleTarget(period, startDate, endDate, warehouseId, franchiseId);
    }
}

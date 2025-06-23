package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.DisposalRateStatisticsResponseBase;
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
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        switch (period) {
            case "DAILY" -> startDate = endDate = baseDate;

            case "WEEKLY" -> {
                startDate = baseDate.with(DayOfWeek.MONDAY);
                endDate = baseDate.with(DayOfWeek.SUNDAY);
                if (today.isBefore(endDate)) {
                    throw new StatisticsException(StatisticsErrorCode.PERIOD_NOT_COMPLETED);
                }
            }

            case "MONTHLY" -> {
                startDate = baseDate.withDayOfMonth(1);
                endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
                if (today.isBefore(endDate)) {
                    throw new StatisticsException(StatisticsErrorCode.PERIOD_NOT_COMPLETED);
                }
            }

            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        }

        if (includeProduct) {
            return mapper.getProductLevelDisposalRate(startDate, endDate, warehouseId, franchiseId);
        } else {
            return mapper.getDisposalRate(startDate, endDate, warehouseId, franchiseId);
        }
    }
}

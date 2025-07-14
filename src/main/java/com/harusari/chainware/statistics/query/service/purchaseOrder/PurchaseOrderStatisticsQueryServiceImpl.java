package com.harusari.chainware.statistics.query.service.purchaseOrder;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderTrendResponse;
import com.harusari.chainware.statistics.query.mapper.PurchaseOrderStatisticsQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderStatisticsQueryServiceImpl implements PurchaseOrderStatisticsQueryService {

    private final PurchaseOrderStatisticsQueryMapper mapper;

    @Override
    @Transactional
    public List<? extends PurchaseOrderStatisticsResponseBase> getStatistics(
            String period, Long vendorId, LocalDate targetDate, boolean includeProduct) {

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
                ? mapper.getProductLevelStatistics(vendorId, startDate, endDate)
                : mapper.getVendorLevelStatistics(vendorId, startDate, endDate);
    }


    @Override
    @Transactional
    public List<PurchaseOrderTrendResponse> getTrend(String period, Long vendorId, LocalDate targetDate) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        return switch (period.toUpperCase()) {
            case "DAILY" -> {
                LocalDate end = baseDate;
                LocalDate start = end.minusDays(6);
                yield mapper.getPurchaseOrderTrendDaily(vendorId, start, end);
            }
            case "WEEKLY" -> {
                LocalDate end = baseDate.with(DayOfWeek.SUNDAY);
                LocalDate start = end.minusWeeks(6).with(DayOfWeek.MONDAY);
                yield mapper.getPurchaseOrderTrendWeekly(vendorId, start, end);
            }
            case "MONTHLY" -> {
                LocalDate end = baseDate.withDayOfMonth(1).plusMonths(1).minusDays(1); // 해당월 말일
                LocalDate start = baseDate.minusMonths(6).withDayOfMonth(1);
                yield mapper.getPurchaseOrderTrendMonthly(vendorId, start, end);
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        };
    }
}

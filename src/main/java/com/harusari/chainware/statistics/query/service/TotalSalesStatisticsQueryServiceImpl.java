package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.TotalSalesStatisticsResponse;
import com.harusari.chainware.statistics.query.mapper.TotalSalesStatisticsQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TotalSalesStatisticsQueryServiceImpl implements TotalSalesStatisticsQueryService {

    private final TotalSalesStatisticsQueryMapper mapper;

    @Override
    @Transactional
    public TotalSalesStatisticsResponse getStatistics(String period, Long franchiseId, LocalDate baseDate) {
        LocalDate target = baseDate != null ? baseDate : LocalDate.now().minusDays(1);

        LocalDate startDate, endDate;
        LocalDate prevStartDate, prevEndDate;

        switch (period.toUpperCase()) {
            case "DAILY" -> {
                startDate = endDate = target;
                prevStartDate = prevEndDate = target.minusDays(1);
            }
            case "WEEKLY" -> {
                startDate = target.with(DayOfWeek.MONDAY);
                endDate = target.with(DayOfWeek.SUNDAY);
                prevStartDate = startDate.minusWeeks(1);
                prevEndDate = endDate.minusWeeks(1);
            }
            case "MONTHLY" -> {
                startDate = target.withDayOfMonth(1);
                endDate = target.withDayOfMonth(target.lengthOfMonth());
                prevStartDate = startDate.minusMonths(1).withDayOfMonth(1);
                prevEndDate = prevStartDate.withDayOfMonth(prevStartDate.lengthOfMonth());
            }
            default -> throw new IllegalArgumentException("지원하지 않는 통계 유형입니다.");
        }

        long total = mapper.getTotalAmount(franchiseId, startDate, endDate);
        long prev = mapper.getTotalAmount(franchiseId, prevStartDate, prevEndDate);

        double rate = (prev == 0) ? 100.0 : ((double)(total - prev) / prev) * 100;
        rate = Math.round(rate * 100.0) / 100.0;

        return new TotalSalesStatisticsResponse(
                startDate,
                franchiseId == null ? "전체" : null,
                total,
                rate
        );
    }
}

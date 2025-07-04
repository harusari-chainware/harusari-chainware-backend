package com.harusari.chainware.statistics.query.service.totalSales;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.totalSales.TotalSalesStatisticsResponse;
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
                endDate = target;
                startDate = target.minusDays(6);
                prevEndDate = target.minusDays(7);
                prevStartDate = target.minusDays(13);
            }
            case "MONTHLY" -> {
                endDate = target;
                startDate = target.minusDays(29);
                prevEndDate = target.minusDays(30);
                prevStartDate = target.minusDays(59);
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
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


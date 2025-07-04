package com.harusari.chainware.statistics.query.service.salesPattern;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.salePattern.DailySalesResponse;
import com.harusari.chainware.statistics.query.dto.salePattern.HourlySalesResponse;
import com.harusari.chainware.statistics.query.dto.salePattern.WeekdaySalesResponse;
import com.harusari.chainware.statistics.query.mapper.SalesPatternMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesPatternQueryServiceImpl implements SalesPatternQueryService {

    private final SalesPatternMapper salesPatternMapper;

    @Override
    @Transactional
    public Object getSalesPattern(String period, Long franchiseId, LocalDate targetDate) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        return switch (period.toUpperCase()) {
            case "HOURLY" -> {
                List<HourlySalesResponse> rawList = (franchiseId != null)
                        ? salesPatternMapper.getHourlySalesByFranchise(franchiseId, baseDate)
                        : salesPatternMapper.getHourlySalesForHeadquarters(baseDate);

                if (rawList.isEmpty()) yield List.of();

                HourlySalesResponse maxItem = rawList.stream()
                        .max(Comparator.comparingLong(HourlySalesResponse::getTotalAmount))
                        .orElseThrow();

                yield rawList.stream()
                        .map(item -> new HourlySalesResponse(
                                item.getHour(),
                                item.getTotalAmount(),
                                item.getHour() == maxItem.getHour()
                        ))
                        .toList();
            }

            case "WEEKLY" -> {
                List<WeekdaySalesResponse> rawList = (franchiseId != null)
                        ? salesPatternMapper.getWeekdaySalesByFranchise(franchiseId, baseDate)
                        : salesPatternMapper.getWeekdaySalesForHeadquarters(baseDate);

                if (rawList.isEmpty()) yield List.of();

                WeekdaySalesResponse maxItem = rawList.stream()
                        .max(Comparator.comparingLong(WeekdaySalesResponse::getAvgAmount))
                        .orElseThrow();

                yield rawList.stream()
                        .map(item -> new WeekdaySalesResponse(
                                item.getWeekday(),
                                item.getDayCount(),
                                item.getTotalAmount(),
                                item.getAvgAmount(),
                                item.getWeekday().equals(maxItem.getWeekday())
                        ))
                        .toList();
            }

            case "MONTHLY" -> {
                List<DailySalesResponse> list = (franchiseId != null)
                        ? salesPatternMapper.getDailySalesByFranchise(franchiseId, baseDate)
                        : salesPatternMapper.getDailySalesForHeadquarters(baseDate);
                yield list;
            }

            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        };
    }
}

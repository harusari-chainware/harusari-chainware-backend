package com.harusari.chainware.statistics.query.service.salesPattern;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.mapper.SalesPatternMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SalesPatternQueryServiceImpl implements SalesPatternQueryService {

    private final SalesPatternMapper salesPatternMapper;

    @Override
    @Transactional
    public Object getSalesPattern(String period, Long franchiseId, LocalDate targetDate) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        return switch (period.toUpperCase()) {
            case "HOURLY" -> (franchiseId != null)
                    ? salesPatternMapper.getHourlySalesByFranchise(franchiseId, baseDate)
                    : salesPatternMapper.getHourlySalesForHeadquarters(baseDate);

            case "WEEKLY" -> (franchiseId != null)
                    ? salesPatternMapper.getWeekdaySalesByFranchise(franchiseId, baseDate)
                    : salesPatternMapper.getWeekdaySalesForHeadquarters(baseDate);

            case "MONTHLY" -> (franchiseId != null)
                    ? salesPatternMapper.getDailySalesByFranchise(franchiseId, baseDate)
                    : salesPatternMapper.getDailySalesForHeadquarters(baseDate);

            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        };
    }
}
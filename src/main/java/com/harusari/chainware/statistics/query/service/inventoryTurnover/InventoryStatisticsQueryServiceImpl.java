package com.harusari.chainware.statistics.query.service.inventoryTurnover;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.invertoryTurnover.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.mapper.InventoryStatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryStatisticsQueryServiceImpl implements InventoryStatisticsQueryService {

    private final InventoryStatisticsMapper inventoryStatisticsMapper;

    @Override
    @Transactional
    public List<InventoryTurnoverResponse> getTurnover(String period, Long franchiseId, LocalDate targetDate) {
        // 기본 날짜는 저번달 1일
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusMonths(1).withDayOfMonth(1);

        if (franchiseId != null) {
            if (!period.equalsIgnoreCase("MONTHLY")) {
                throw new StatisticsException(StatisticsErrorCode.INVALID_PERIOD_FOR_FRANCHISE);
            }
            return getFranchiseMonthlyTurnover(franchiseId, baseDate);
        }

        return switch (period.toUpperCase()) {
            case "WEEKLY" -> getWeeklyTurnover(baseDate);
            case "MONTHLY" -> getMonthlyTurnover(baseDate);
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        };
    }

    private List<InventoryTurnoverResponse> getFranchiseMonthlyTurnover(Long franchiseId, LocalDate baseDate) {
        LocalDate startDate = baseDate.withDayOfMonth(1);
        LocalDate endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
        return inventoryStatisticsMapper.getFranchiseMonthlyTurnover(franchiseId, startDate, endDate);
    }

    private List<InventoryTurnoverResponse> getWeeklyTurnover(LocalDate baseDate) {
        LocalDate startDate = baseDate.with(DayOfWeek.MONDAY);
        LocalDate endDate = baseDate.with(DayOfWeek.SUNDAY);
        return inventoryStatisticsMapper.getWeeklyTurnover(startDate, endDate);
    }

    private List<InventoryTurnoverResponse> getMonthlyTurnover(LocalDate baseDate) {
        LocalDate startDate = baseDate.withDayOfMonth(1);
        LocalDate endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
        return inventoryStatisticsMapper.getMonthlyTurnover(startDate, endDate);
    }
}

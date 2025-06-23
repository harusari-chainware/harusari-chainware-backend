package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.mapper.InventoryStatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                throw new IllegalArgumentException("가맹점은 월간(MONTHLY) 회전율 조회만 지원합니다.");
            }
            return getFranchiseMonthlyTurnover(franchiseId, baseDate);
        }

        return switch (period.toUpperCase()) {
            case "WEEKLY" -> getWeeklyTurnover(baseDate);
            case "MONTHLY" -> getMonthlyTurnover(baseDate);
            default -> throw new IllegalArgumentException("지원하지 않는 기간 유형입니다. (WEEKLY 또는 MONTHLY만 허용)");
        };
    }

    private List<InventoryTurnoverResponse> getFranchiseMonthlyTurnover(Long franchiseId, LocalDate baseDate) {
        LocalDate startDate = baseDate.withDayOfMonth(1);
        LocalDate endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
        return inventoryStatisticsMapper.getFranchiseMonthlyTurnover(franchiseId, startDate, endDate);
    }

    private List<InventoryTurnoverResponse> getWeeklyTurnover(LocalDate baseDate) {
        LocalDate startDate = baseDate.with(java.time.DayOfWeek.MONDAY);
        LocalDate endDate = baseDate.with(java.time.DayOfWeek.SUNDAY);
        return inventoryStatisticsMapper.getWeeklyTurnover(startDate, endDate);
    }

    private List<InventoryTurnoverResponse> getMonthlyTurnover(LocalDate baseDate) {
        LocalDate startDate = baseDate.withDayOfMonth(1);
        LocalDate endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
        return inventoryStatisticsMapper.getMonthlyTurnover(startDate, endDate);
    }
}

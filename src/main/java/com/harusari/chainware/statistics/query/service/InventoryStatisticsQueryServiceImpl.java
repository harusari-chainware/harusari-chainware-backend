package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.mapper.InventoryStatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryStatisticsQueryServiceImpl implements InventoryStatisticsQueryService {

    private final InventoryStatisticsMapper inventoryStatisticsMapper;

    @Override
    @Transactional
    public List<InventoryTurnoverResponse> getMonthlyTurnover(LocalDate targetDate) {
        LocalDate now = LocalDate.now();

        if (targetDate == null) {
            YearMonth lastMonth = YearMonth.from(now.minusMonths(1));
            LocalDate start = lastMonth.atDay(1);
            LocalDate end = lastMonth.atEndOfMonth();
            return inventoryStatisticsMapper.selectWarehouseTurnoverMonthly(start, end);
        }

        YearMonth targetMonth = YearMonth.from(targetDate);
        YearMonth currentMonth = YearMonth.from(now);

        if (targetMonth.equals(currentMonth)) {
            throw new IllegalArgumentException("해당 월은 아직 종료되지 않아 회전율 통계를 조회할 수 없습니다.");
        }

        LocalDate start = targetMonth.atDay(1);
        LocalDate end = targetMonth.atEndOfMonth();
        return inventoryStatisticsMapper.selectWarehouseTurnoverMonthly(start, end);
    }

    @Override
    @Transactional
    public List<InventoryTurnoverResponse> getWeeklyTurnover(LocalDate targetDate) {
        LocalDate now = LocalDate.now();

        if (targetDate == null) {
            LocalDate lastWeekSunday = now.with(DayOfWeek.SUNDAY).minusWeeks(1);
            LocalDate start = lastWeekSunday.minusDays(6);
            return inventoryStatisticsMapper.selectWarehouseTurnoverWeekly(start, lastWeekSunday);
        }

        LocalDate targetWeekStart = targetDate.with(DayOfWeek.MONDAY);
        LocalDate targetWeekEnd = targetWeekStart.plusDays(6);
        LocalDate currentWeekStart = now.with(DayOfWeek.MONDAY);

        if (!targetWeekEnd.isBefore(now)) {
            throw new IllegalArgumentException("해당 주는 아직 종료되지 않아 회전율 통계를 조회할 수 없습니다.");
        }

        return inventoryStatisticsMapper.selectWarehouseTurnoverWeekly(targetWeekStart, targetWeekEnd);
    }

    @Override
    @Transactional
    public List<InventoryTurnoverResponse> getFranchiseMonthlyTurnover(Long franchiseId, LocalDate targetDate) {
        if (franchiseId == null) {
            throw new IllegalArgumentException("franchiseId는 필수입니다.");
        }

        LocalDate now = LocalDate.now();
        YearMonth yearMonth = (targetDate != null) ? YearMonth.from(targetDate) : YearMonth.from(now.minusMonths(1));

        if (yearMonth.equals(YearMonth.from(now))) {
            throw new IllegalArgumentException("해당 월은 아직 종료되지 않아 회전율 통계를 조회할 수 없습니다.");
        }

        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        return inventoryStatisticsMapper.selectFranchiseTurnoverWithBom(franchiseId, start, end);
    }
}

package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.StoreOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.mapper.StoreOrderStatisticsQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreOrderStatisticsQueryServiceImpl implements StoreOrderStatisticsQueryService {

    private final StoreOrderStatisticsQueryMapper mapper;

    @Override
    @Transactional
    public List<? extends StoreOrderStatisticsResponseBase> getStatistics(
            String period, Long franchiseId, LocalDate targetDate, boolean includeProduct) {

        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);
        LocalDate today = LocalDate.now();
        LocalDate startDate, endDate;

        switch (period.toUpperCase()) {
            case "DAILY" -> startDate = endDate = baseDate;
            case "WEEKLY" -> {
                startDate = baseDate.with(DayOfWeek.MONDAY);
                endDate = baseDate.with(DayOfWeek.SUNDAY);
                if (today.isBefore(endDate)) {
                    throw new IllegalArgumentException("이번 주는 아직 완료되지 않아 통계를 조회할 수 없습니다.");
                }
            }
            case "MONTHLY" -> {
                startDate = baseDate.withDayOfMonth(1);
                endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
                if (today.isBefore(endDate)) {
                    throw new IllegalArgumentException("이번 달은 아직 완료되지 않아 통계를 조회할 수 없습니다.");
                }
            }
            default -> throw new IllegalArgumentException("지원하지 않는 통계 유형입니다.");
        }

        return includeProduct
                ? mapper.getProductLevelStatistics(franchiseId, startDate, endDate)
                : mapper.getFranchiseLevelStatistics(franchiseId, startDate, endDate);
    }
}

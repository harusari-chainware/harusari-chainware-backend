package com.harusari.chainware.statistics.query.service.inventoryTurnover;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverTrendResponse;
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
    public List<InventoryTurnoverResponse> getTurnover(String period, Long franchiseId, Long warehouseId, LocalDate targetDate) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        if (franchiseId != null) {
            if (!period.equalsIgnoreCase("MONTHLY")) {
                throw new StatisticsException(StatisticsErrorCode.INVALID_PERIOD_FOR_FRANCHISE);
            }
            return getFranchiseMonthlyTurnover(franchiseId, baseDate);
        }

        LocalDate startDate, endDate = baseDate;
        if (warehouseId != null) {
            return switch (period.toUpperCase()) {
                case "DAILY" -> inventoryStatisticsMapper.getDailyTurnoverByWarehouse(baseDate, endDate, warehouseId);
                case "WEEKLY" -> {
                    startDate = baseDate.minusDays(6);
                    yield inventoryStatisticsMapper.getWeeklyTurnoverByWarehouse(startDate, endDate, warehouseId);
                }
                case "MONTHLY" -> {
                    startDate = baseDate.minusDays(29);
                    yield inventoryStatisticsMapper.getMonthlyTurnoverByWarehouse(startDate, endDate, warehouseId);
                }
                default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
            };
        }

        return switch (period.toUpperCase()) {
            case "DAILY" -> {
                startDate = baseDate;
                yield inventoryStatisticsMapper.getDailyTurnover(startDate, endDate);
            }
            case "WEEKLY" -> {
                startDate = baseDate.minusDays(6);
                yield inventoryStatisticsMapper.getWeeklyTurnover(startDate, endDate);
            }
            case "MONTHLY" -> {
                startDate = baseDate.minusDays(29);
                yield inventoryStatisticsMapper.getMonthlyTurnover(startDate, endDate);
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD);
        };
    }

    private List<InventoryTurnoverResponse> getFranchiseMonthlyTurnover(Long franchiseId, LocalDate baseDate) {
        LocalDate startDate = baseDate.withDayOfMonth(1);
        LocalDate endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
        return inventoryStatisticsMapper.getFranchiseMonthlyTurnover(franchiseId, startDate, endDate);
    }

    @Override
    @Transactional
    public List<InventoryTurnoverTrendResponse> getTrend(String period, Long franchiseId, Long warehouseId, LocalDate targetDate) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        LocalDate startDate, endDate = baseDate;
        if (franchiseId != null) {
            if (!period.equalsIgnoreCase("MONTHLY")) {
                throw new StatisticsException(StatisticsErrorCode.INVALID_PERIOD_FOR_FRANCHISE);
            }
            startDate = baseDate.withDayOfMonth(1).minusMonths(6);
            endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
            return inventoryStatisticsMapper.getFranchiseMonthlyTurnoverTrend(startDate, endDate, franchiseId);
        }

        if (warehouseId != null) {
            return switch (period.toUpperCase()) {
                case "DAILY" -> {
                    startDate = baseDate.minusDays(6);
                    yield inventoryStatisticsMapper.getDailyTurnoverTrend(startDate, endDate, warehouseId);
                }
                case "WEEKLY" -> {
                    startDate = baseDate.minusWeeks(6);
                    yield inventoryStatisticsMapper.getWeeklyTurnoverTrendByWarehouse(startDate, endDate, warehouseId);
                }
                case "MONTHLY" -> {
                    startDate = baseDate.minusMonths(6).withDayOfMonth(1);
                    endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
                    yield inventoryStatisticsMapper.getMonthlyTurnoverTrendByWarehouse(startDate, endDate, warehouseId);
                }
                default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD_TYPE);
            };
        }

        return switch (period.toUpperCase()) {
            case "DAILY" -> {
                startDate = baseDate.minusDays(6);
                yield inventoryStatisticsMapper.getDailyTurnoverTrend(startDate, endDate, null);
            }
            case "WEEKLY" -> {
                startDate = baseDate.minusWeeks(6);
                yield inventoryStatisticsMapper.getWeeklyTurnoverTrend(startDate, endDate, null);
            }
            case "MONTHLY" -> {
                startDate = baseDate.minusMonths(6).withDayOfMonth(1);
                endDate = baseDate.withDayOfMonth(baseDate.lengthOfMonth());
                yield inventoryStatisticsMapper.getMonthlyTurnoverTrend(startDate, endDate, null);
            }
            default -> throw new StatisticsException(StatisticsErrorCode.UNSUPPORTED_PERIOD_TYPE);
        };
    }
}

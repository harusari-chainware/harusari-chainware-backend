package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.InventoryTurnoverResponse;

import java.time.LocalDate;
import java.util.List;

public interface InventoryStatisticsQueryService {

    List<InventoryTurnoverResponse> getMonthlyTurnover(LocalDate targetDate);

    List<InventoryTurnoverResponse> getWeeklyTurnover(LocalDate targetDate);

    List<InventoryTurnoverResponse> getFranchiseMonthlyTurnover(Long franchiseId, LocalDate targetDate);

}

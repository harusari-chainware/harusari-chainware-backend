package com.harusari.chainware.statistics.query.service.inventoryTurnover;

import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverTrendResponse;

import java.time.LocalDate;
import java.util.List;

public interface InventoryStatisticsQueryService {

    List<InventoryTurnoverResponse> getTurnover(String period, Long franchiseId, LocalDate targetDate);

    List<InventoryTurnoverTrendResponse> getTrend(String period, Long franchiseId, LocalDate targetDate);

}

package com.harusari.chainware.statistics.query.service.inventoryTurnover;

import com.harusari.chainware.statistics.query.dto.invertoryTurnover.InventoryTurnoverResponse;

import java.time.LocalDate;
import java.util.List;

public interface InventoryStatisticsQueryService {

    List<InventoryTurnoverResponse> getTurnover(String period, Long franchiseId, LocalDate targetDate);

}

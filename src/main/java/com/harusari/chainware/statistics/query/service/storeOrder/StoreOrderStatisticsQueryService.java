package com.harusari.chainware.statistics.query.service.storeOrder;

import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderTrendResponse;

import java.time.LocalDate;
import java.util.List;

public interface StoreOrderStatisticsQueryService {

    List<? extends StoreOrderStatisticsResponseBase> getStatistics(
            String period, Long franchiseId, LocalDate targetDate, boolean includeProduct);

    List<StoreOrderTrendResponse> getTrend(String period, Long franchiseId, LocalDate targetDate);
}

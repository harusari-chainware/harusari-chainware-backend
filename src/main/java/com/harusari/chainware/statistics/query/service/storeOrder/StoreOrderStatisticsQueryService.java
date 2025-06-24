package com.harusari.chainware.statistics.query.service.storeOrder;

import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderStatisticsResponseBase;

import java.time.LocalDate;
import java.util.List;

public interface StoreOrderStatisticsQueryService {

    List<? extends StoreOrderStatisticsResponseBase> getStatistics(
            String period, Long franchiseId, LocalDate targetDate, boolean includeProduct);

}

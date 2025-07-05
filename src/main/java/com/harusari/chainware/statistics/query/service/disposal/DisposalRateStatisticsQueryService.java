package com.harusari.chainware.statistics.query.service.disposal;

import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateTrendGroupedResponse;

import java.time.LocalDate;
import java.util.List;

public interface DisposalRateStatisticsQueryService {

    List<? extends DisposalRateStatisticsResponseBase> getDisposalStatistics(
            String period, Long warehouseId, Long franchiseId, LocalDate targetDate, boolean includeProduct);

    DisposalRateTrendGroupedResponse getGroupedTrend(String period, LocalDate targetDate);

}

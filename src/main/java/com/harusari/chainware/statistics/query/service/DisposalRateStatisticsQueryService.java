package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.DisposalRateStatisticsResponseBase;

import java.time.LocalDate;
import java.util.List;

public interface DisposalRateStatisticsQueryService {

    List<? extends DisposalRateStatisticsResponseBase> getDisposalStatistics(
            String period, Long warehouseId, Long franchiseId, LocalDate targetDate, boolean includeProduct);

}

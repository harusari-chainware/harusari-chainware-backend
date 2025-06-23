package com.harusari.chainware.statistics.query.service.totalSales;

import com.harusari.chainware.statistics.query.dto.totalSales.TotalSalesStatisticsResponse;

import java.time.LocalDate;

public interface TotalSalesStatisticsQueryService {

    TotalSalesStatisticsResponse getStatistics(String period, Long franchiseId, LocalDate targetDate);
}

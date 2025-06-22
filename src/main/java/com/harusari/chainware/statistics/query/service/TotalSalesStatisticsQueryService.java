package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.TotalSalesStatisticsResponse;

import java.time.LocalDate;

public interface TotalSalesStatisticsQueryService {

    TotalSalesStatisticsResponse getStatistics(String period, Long franchiseId, LocalDate targetDate);
}

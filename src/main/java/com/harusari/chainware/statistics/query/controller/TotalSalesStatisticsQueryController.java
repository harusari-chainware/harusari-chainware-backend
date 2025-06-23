package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.totalSales.TotalSalesStatisticsResponse;
import com.harusari.chainware.statistics.query.service.totalSales.TotalSalesStatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statistics/total-sales")
@RequiredArgsConstructor
public class TotalSalesStatisticsQueryController {

    private final TotalSalesStatisticsQueryService service;

    @GetMapping
    public ResponseEntity<ApiResponse<TotalSalesStatisticsResponse>> getTotalSalesStatistics(
            @RequestParam(defaultValue = "DAILY") String period,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        TotalSalesStatisticsResponse result = service.getStatistics(period.toUpperCase(), franchiseId, targetDate);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

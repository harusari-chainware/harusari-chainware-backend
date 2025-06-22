package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.TotalSalesStatisticsResponse;
import com.harusari.chainware.statistics.query.service.TotalSalesStatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statistics/total-sales")
@RequiredArgsConstructor
public class TotalSalesStatisticsQueryController {

    private final TotalSalesStatisticsQueryService service;

    @GetMapping
    public TotalSalesStatisticsResponse getTotalSalesStatistics(
            @RequestParam(defaultValue = "DAILY") String period,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return service.getStatistics(period.toUpperCase(), franchiseId, targetDate);
    }
}

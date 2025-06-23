package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.DisposalRateStatisticsResponseBase;
import com.harusari.chainware.statistics.query.service.DisposalRateStatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/disposal-rate")
@RequiredArgsConstructor
public class DisposalRateStatisticsQueryController {

    private final DisposalRateStatisticsQueryService service;

    @GetMapping
    public ApiResponse<List<? extends DisposalRateStatisticsResponseBase>> getDisposalRate(
            @RequestParam String period,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
            @RequestParam(defaultValue = "false") boolean includeProduct
    ) {
        List<? extends DisposalRateStatisticsResponseBase> result =
                service.getDisposalStatistics(period.toUpperCase(), warehouseId, franchiseId, targetDate, includeProduct);
        return ApiResponse.success(result);
    }
}
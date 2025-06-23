package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.service.InventoryStatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/inventory-turnover")
@RequiredArgsConstructor
public class InventoryStatisticsQueryController {

    private final InventoryStatisticsQueryService inventoryStatisticsQueryService;

    @GetMapping("/monthly")
    public ApiResponse<List<InventoryTurnoverResponse>> getMonthlyTurnover(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        List<InventoryTurnoverResponse> result = inventoryStatisticsQueryService.getMonthlyTurnover(targetDate);
        return ApiResponse.success(result);
    }

    @GetMapping("/weekly")
    public ApiResponse<List<InventoryTurnoverResponse>> getWeeklyTurnover(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        List<InventoryTurnoverResponse> result = inventoryStatisticsQueryService.getWeeklyTurnover(targetDate);
        return ApiResponse.success(result);
    }

    @GetMapping("/monthly/franchise")
    public ApiResponse<List<InventoryTurnoverResponse>> getFranchiseTurnover(
            @RequestParam Long franchiseId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        List<InventoryTurnoverResponse> result = inventoryStatisticsQueryService.getFranchiseMonthlyTurnover(franchiseId, targetDate);
        return ApiResponse.success(result);
    }
}

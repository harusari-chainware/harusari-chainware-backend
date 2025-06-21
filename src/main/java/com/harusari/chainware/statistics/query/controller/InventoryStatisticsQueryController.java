package com.harusari.chainware.statistics.query.controller;

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
    public List<InventoryTurnoverResponse> getMonthlyTurnover(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return inventoryStatisticsQueryService.getMonthlyTurnover(targetDate);
    }

    @GetMapping("/weekly")
    public List<InventoryTurnoverResponse> getWeeklyTurnover(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return inventoryStatisticsQueryService.getWeeklyTurnover(targetDate);
    }

    @GetMapping("/monthly/franchise")
    public List<InventoryTurnoverResponse> getFranchiseTurnover(
            @RequestParam Long franchiseId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return inventoryStatisticsQueryService.getFranchiseMonthlyTurnover(franchiseId, targetDate);
    }
}
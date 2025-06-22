package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.StoreOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.service.StoreOrderStatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/store-order")
@RequiredArgsConstructor
public class StoreOrderStatisticsQueryController {

    private final StoreOrderStatisticsQueryService storeOrderStatisticsQueryService;

    @GetMapping
    public ApiResponse<List<? extends StoreOrderStatisticsResponseBase>> getStoreOrderStatistics(
            @RequestParam String period,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
            @RequestParam(defaultValue = "false") boolean includeProduct
    ) {
        List<? extends StoreOrderStatisticsResponseBase> result =
                storeOrderStatisticsQueryService.getStatistics(
                        period.toUpperCase(), franchiseId, targetDate, includeProduct
                );

        return ApiResponse.success(result);
    }
}

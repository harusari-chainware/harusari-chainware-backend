package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.service.SalesPatternQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statistics/patterns")
@RequiredArgsConstructor
public class SalesPatternQueryController {

    private final SalesPatternQueryService salesPatternQueryService;

    @GetMapping
    public ApiResponse<?> getSalesPattern(
            @RequestParam(defaultValue = "HOURLY") String period,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return ApiResponse.success(salesPatternQueryService.getSalesPattern(period, franchiseId, targetDate));
    }
}
package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.HourlySalesResponse;
import com.harusari.chainware.statistics.query.service.SalesPatternQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/patterns")
@RequiredArgsConstructor
public class SalesPatternQueryController {

    private final SalesPatternQueryService salesPatternQueryService;

    @GetMapping("/hourly")
    public List<HourlySalesResponse> getHourlySales(
            @RequestParam Long franchiseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return salesPatternQueryService.getHourlySalesByFranchise(franchiseId, targetDate);
    }
}
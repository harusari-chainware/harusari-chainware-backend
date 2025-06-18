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
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        LocalDate date = (targetDate != null) ? targetDate : LocalDate.now();

        if (franchiseId != null) {
            // 가맹점 단위
            return salesPatternQueryService.getHourlySalesByFranchise(franchiseId, date);
        } else {
            // 본사 전체 가맹점 기준
            return salesPatternQueryService.getHourlySalesForHeadquarters(date);
        }
    }
}

package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.DailySalesResponse;
import com.harusari.chainware.statistics.query.dto.HourlySalesResponse;
import com.harusari.chainware.statistics.query.dto.WeekdaySalesResponse;
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

    // 시간대별
    @GetMapping("/hourly")
    public List<HourlySalesResponse> getHourlySales(
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        LocalDate date = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        if (franchiseId != null) {
            return salesPatternQueryService.getHourlySalesByFranchise(franchiseId, date);
        } else {
            return salesPatternQueryService.getHourlySalesForHeadquarters(date);
        }
    }

    // 요일별(7일) 매출 평균 통계
    @GetMapping("/weekday")
    public List<WeekdaySalesResponse> getWeekdaySales(
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        if (franchiseId != null) {
            return salesPatternQueryService.getWeekdaySalesByFranchise(franchiseId, baseDate);
        } else {
            return salesPatternQueryService.getWeekdaySalesForHeadquarters(baseDate);
        }
    }

    // 요일별(30) 매출 통계 추이
    @GetMapping("/daily")
    public List<DailySalesResponse> getDailySales(
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        LocalDate baseDate = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        if (franchiseId != null) {
            return salesPatternQueryService.getDailySalesByFranchise(franchiseId, baseDate);
        } else {
            return salesPatternQueryService.getDailySalesForHeadquarters(baseDate);
        }
    }
}

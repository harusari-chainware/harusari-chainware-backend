package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.MenuSalesResponse;
import com.harusari.chainware.statistics.query.service.MenuSalesQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/menu-sales")
@RequiredArgsConstructor
public class MenuSalesQueryController {

    private final MenuSalesQueryService menuSalesQueryService;

    @GetMapping
    public List<MenuSalesResponse> getMenuSalesByPeriod(
            @RequestParam Long franchiseId,
            @RequestParam String periodType,  // DAILY, WEEKLY, MONTHLY
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return menuSalesQueryService.getMenuSalesByPeriod(franchiseId, periodType, targetDate);
    }

    @GetMapping("/headquarters")
    public List<MenuSalesResponse> getMenuSalesForHQ(
            @RequestParam String periodType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return menuSalesQueryService.getMenuSalesForHeadquarters(periodType, targetDate);
    }

}

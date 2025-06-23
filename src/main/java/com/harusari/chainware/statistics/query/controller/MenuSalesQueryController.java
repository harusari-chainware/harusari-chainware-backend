package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
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
    public ApiResponse<List<MenuSalesResponse>> getMenuSales(
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(defaultValue = "DAILY") String periodType,    // DAILY, WEEKLY, MONTHLY
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        LocalDate date = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        List<MenuSalesResponse> result;
        if (franchiseId != null) {
            // 가맹점용
            result = menuSalesQueryService.getMenuSalesByPeriod(franchiseId, periodType, date);
        } else {
            // 본사용
            result = menuSalesQueryService.getMenuSalesForHeadquarters(periodType, date);
        }

        return ApiResponse.success(result);
    }
}
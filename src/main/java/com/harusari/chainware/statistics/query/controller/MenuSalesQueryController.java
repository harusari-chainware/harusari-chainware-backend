package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.menuSales.MenuSalesResponse;
import com.harusari.chainware.statistics.query.service.menuSales.MenuSalesQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/menu-sales")
@RequiredArgsConstructor
public class MenuSalesQueryController {

    private final MenuSalesQueryService menuSalesQueryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuSalesResponse>>> getMenuSales(
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(defaultValue = "DAILY") String periodType, // DAILY, WEEKLY, MONTHLY
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        LocalDate date = (targetDate != null) ? targetDate : LocalDate.now().minusDays(1);

        List<MenuSalesResponse> result = (franchiseId != null)
                ? menuSalesQueryService.getMenuSalesByPeriod(franchiseId, periodType, date)
                : menuSalesQueryService.getMenuSalesForHeadquarters(periodType, date);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

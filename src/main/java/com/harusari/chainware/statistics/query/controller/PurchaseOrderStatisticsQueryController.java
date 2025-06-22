package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.PurchaseOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.service.PurchaseOrderStatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/purchase-order")
@RequiredArgsConstructor
public class PurchaseOrderStatisticsQueryController {

    private final PurchaseOrderStatisticsQueryService purchaseOrderStatisticsQueryService;

    @GetMapping
    public List<? extends PurchaseOrderStatisticsResponseBase> getStatistics(
            @RequestParam String period,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
            @RequestParam(defaultValue = "false") boolean includeProduct
    ) {
        return purchaseOrderStatisticsQueryService.getStatistics(
                period.toUpperCase(), vendorId, targetDate, includeProduct);
    }
}

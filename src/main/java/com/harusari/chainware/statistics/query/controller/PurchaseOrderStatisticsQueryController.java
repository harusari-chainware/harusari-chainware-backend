package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.service.purchaseOrder.PurchaseOrderStatisticsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<List<? extends PurchaseOrderStatisticsResponseBase>>> getStatistics(
            @RequestParam String period,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
            @RequestParam(defaultValue = "false") boolean includeProduct
    ) {
        List<? extends PurchaseOrderStatisticsResponseBase> result =
                purchaseOrderStatisticsQueryService.getStatistics(
                        period.toUpperCase(), vendorId, targetDate, includeProduct);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

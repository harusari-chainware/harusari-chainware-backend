package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.totalSales.TotalSalesStatisticsResponse;
import com.harusari.chainware.statistics.query.service.totalSales.TotalSalesStatisticsQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statistics/total-sales")
@RequiredArgsConstructor
@Tag(name = "총 매출 통계 API", description = "기간별 총 매출 및 전 기간 대비 증감률을 조회하는 API입니다.")
public class TotalSalesStatisticsQueryController {

    private final TotalSalesStatisticsQueryService service;

    @Operation(
            summary = "총 매출 통계 조회",
            description = """
            전체 또는 특정 가맹점의 총 매출을 조회합니다.  
            - `DAILY`: 기준일과 전일 매출 비교  
            - `WEEKLY`: 기준 주(월~일)와 전 주 매출 비교  
            - `MONTHLY`: 기준 월과 전월 매출 비교  
            - `franchiseId`가 없으면 전체 기준, 있으면 해당 가맹점 기준  
            - `targetDate`가 없으면 기본값은 어제입니다.
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "총 매출 통계 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<TotalSalesStatisticsResponse>> getTotalSalesStatistics(
            @Parameter(description = "조회 단위 (DAILY, WEEKLY, MONTHLY)", example = "DAILY")
            @RequestParam(defaultValue = "DAILY") String period,

            @Parameter(description = "가맹점 ID (없으면 전체 기준)", example = "7")
            @RequestParam(required = false) Long franchiseId,

            @Parameter(description = "기준일 (기본값: 어제)", example = "2025-06-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        TotalSalesStatisticsResponse result = service.getStatistics(period.toUpperCase(), franchiseId, targetDate);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

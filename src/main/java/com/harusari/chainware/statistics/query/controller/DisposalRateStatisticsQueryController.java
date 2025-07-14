package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponse;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateTrendGroupedResponse;
import com.harusari.chainware.statistics.query.service.disposal.DisposalRateStatisticsQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/disposal-rate")
@RequiredArgsConstructor
@Tag(name = "폐기율 통계 API", description = "폐기율 통계를 조회하는 API입니다.")
public class DisposalRateStatisticsQueryController {

    private final DisposalRateStatisticsQueryService service;

    @Operation(
            summary = "폐기율 통계 조회",
            description = """
            폐기율 통계를 조회합니다. 일간/주간/월간 단위로 전체, 본사 창고, 가맹점 단위의 폐기율 변화량을 조회할 수 있습니다.  
            - period는 `WEEKLY` 또는 `MONTHLY`, `DAILY` 중 하나  
            - warehouseId 또는 franchiseId를 지정해 특정 단위로 조회 가능  
            - includeProduct=true 설정 시 상품별 폐기율 포함
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "폐기율 통계 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<List<? extends DisposalRateStatisticsResponseBase>>> getDisposalRate(
            @Parameter(description = "조회 기간 단위 (WEEKLY, MONTHLY, DAILY)", required = true, example = "DAILY")
            @RequestParam String period,

            @Parameter(description = "창고 ID (warehouse 단위 조회 시 사용)", example = "1")
            @RequestParam(required = false) Long warehouseId,

            @Parameter(description = "가맹점 ID (franchise 단위 조회 시 사용)", example = "2")
            @RequestParam(required = false) Long franchiseId,

            @Parameter(description = "기준 날짜 (기본값: 어제 날짜)", example = "2025-06-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,

            @Parameter(description = "상품별 폐기율 포함 여부 (true/false)", example = "false")
            @RequestParam(defaultValue = "false") boolean includeProduct
    ) {
        List<? extends DisposalRateStatisticsResponseBase> result =
                service.getDisposalStatistics(period.toUpperCase(), warehouseId, franchiseId, targetDate, includeProduct);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "폐기율 추이 조회 (전체/단일 가맹점 또는 창고)")
    @GetMapping("/trend")
    public ResponseEntity<ApiResponse<?>> getTrend(
            @RequestParam String period,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        if (warehouseId == null && franchiseId == null) {
            // 전체, 본사, 가맹점 세트 조회
            return ResponseEntity.ok(ApiResponse.success(
                    service.getGroupedTrend(period.toUpperCase(), targetDate)
            ));
        } else {
            // 단일 대상 조회 (7일, 7주, 7개월)
            return ResponseEntity.ok(ApiResponse.success(
                    service.getSingleTrend(period.toUpperCase(), warehouseId, franchiseId, targetDate)
            ));
        }
    }
}

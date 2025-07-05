package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderTrendResponse;
import com.harusari.chainware.statistics.query.service.purchaseOrder.PurchaseOrderStatisticsQueryService;
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
@RequestMapping("/api/v1/statistics/purchase-order")
@RequiredArgsConstructor
@Tag(name = "거래처 발주 통계 API", description = "거래처 또는 상품 단위로 발주 통계를 조회하는 API입니다.")
public class PurchaseOrderStatisticsQueryController {

    private final PurchaseOrderStatisticsQueryService purchaseOrderStatisticsQueryService;

    @Operation(
            summary = "거래처 발주 통계 조회",
            description = """
            거래처 발주 통계를 일/주/월 단위로 조회합니다.  
            - `DAILY`: 기준일 하루만 조회  
            - `WEEKLY`: 기준일이 포함된 주 (월~일) 조회 (해당 주가 완료되지 않으면 예외 발생)  
            - `MONTHLY`: 기준일이 속한 월 전체 조회 (해당 월이 완료되지 않으면 예외 발생)  
            - `vendorId` 지정 시 특정 거래처 필터링  
            - `includeProduct=true` 설정 시 상품 단위로 상세 통계 조회
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "거래처 발주 통계 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<List<? extends PurchaseOrderStatisticsResponseBase>>> getStatistics(
            @Parameter(description = "조회 단위 (DAILY, WEEKLY, MONTHLY)", example = "WEEKLY")
            @RequestParam String period,

            @Parameter(description = "거래처 ID (지정 시 해당 거래처만 필터링)", example = "42")
            @RequestParam(required = false) Long vendorId,

            @Parameter(description = "기준일 (기본값: 어제)", example = "2025-06-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,

            @Parameter(description = "상품 단위 상세 통계 포함 여부", example = "false")
            @RequestParam(defaultValue = "false") boolean includeProduct
    ) {
        List<? extends PurchaseOrderStatisticsResponseBase> result =
                purchaseOrderStatisticsQueryService.getStatistics(
                        period.toUpperCase(), vendorId, targetDate, includeProduct);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/trend")
    @Operation(summary = "발주 추이 조회", description = "기간별(주간/월간) 발주량/금액 변화를 일자별로 반환합니다. DAILY는 지원하지 않습니다.")
    public ResponseEntity<ApiResponse<List<PurchaseOrderTrendResponse>>> getTrend(
            @RequestParam String period,
            @RequestParam(required = false) Long vendorId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        if ("DAILY".equalsIgnoreCase(period)) {
            return ResponseEntity.ok(ApiResponse.success(List.of()));
        }

        List<PurchaseOrderTrendResponse> trend =
                purchaseOrderStatisticsQueryService.getTrend(period.toUpperCase(), vendorId, targetDate);

        return ResponseEntity.ok(ApiResponse.success(trend));
    }
}

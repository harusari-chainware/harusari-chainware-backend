package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderTrendResponse;
import com.harusari.chainware.statistics.query.service.storeOrder.StoreOrderStatisticsQueryService;
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
@RequestMapping("/api/v1/statistics/store-order")
@RequiredArgsConstructor
@Tag(name = "가맹점 주문 통계 API", description = "가맹점 또는 상품 단위로 주문량 통계를 조회하는 API입니다.")
public class StoreOrderStatisticsQueryController {

    private final StoreOrderStatisticsQueryService storeOrderStatisticsQueryService;

    @Operation(
            summary = "가맹점 주문 통계 조회",
            description = """
            가맹점 주문 통계를 조회합니다.  
            - `DAILY`: 하루 기준  
            - `WEEKLY`: 해당 주(월~일) 기준 (해당 주가 완료되지 않으면 예외 발생)  
            - `MONTHLY`: 해당 월 전체 기준 (해당 월이 완료되지 않으면 예외 발생)  
            - `franchiseId`가 없으면 전체 기준, 있으면 해당 가맹점 기준  
            - `includeProduct=true` 설정 시 상품 단위로 상세 통계 반환
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 통계 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<List<? extends StoreOrderStatisticsResponseBase>>> getStoreOrderStatistics(
            @Parameter(description = "조회 단위 (DAILY, WEEKLY, MONTHLY)", example = "DAILY")
            @RequestParam String period,

            @Parameter(description = "가맹점 ID (지정 시 해당 가맹점 기준)", example = "10")
            @RequestParam(required = false) Long franchiseId,

            @Parameter(description = "기준일 (기본값: 어제)", example = "2025-06-30")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,

            @Parameter(description = "상품 단위 통계 포함 여부", example = "false")
            @RequestParam(defaultValue = "false") boolean includeProduct
    ) {
        List<? extends StoreOrderStatisticsResponseBase> result =
                storeOrderStatisticsQueryService.getStatistics(
                        period.toUpperCase(), franchiseId, targetDate, includeProduct
                );

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/trend")
    @Operation(summary = "가맹점 주문 추이 조회", description = "기간별 가맹점 주문량 추이를 일자별로 반환합니다. DAILY는 제외됩니다.")
    public ResponseEntity<ApiResponse<List<StoreOrderTrendResponse>>> getOrderTrend(
            @RequestParam String period,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        if ("DAILY".equalsIgnoreCase(period)) {
            return ResponseEntity.ok(ApiResponse.success(List.of()));
        }
        return ResponseEntity.ok(ApiResponse.success(
                storeOrderStatisticsQueryService.getTrend(period.toUpperCase(), franchiseId, targetDate)
        ));
    }

}

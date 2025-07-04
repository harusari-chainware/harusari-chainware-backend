package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverTrendResponse;
import com.harusari.chainware.statistics.query.service.inventoryTurnover.InventoryStatisticsQueryService;
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
@RequestMapping("/api/v1/statistics/inventory-turnover")
@RequiredArgsConstructor
@Tag(name = "재고 회전율 통계 API", description = "가맹점 또는 전체의 재고 회전율을 조회하는 API입니다.")
public class InventoryStatisticsQueryController {

    private final InventoryStatisticsQueryService inventoryStatisticsQueryService;

    @Operation(
            summary = "재고 회전율 통계 조회",
            description = """
            재고 회전율 통계를 조회합니다.  
            - 본사 전체의 경우 period는 `WEEKLY` 또는 `MONTHLY` 중 선택 가능  
            - 특정 가맹점(`franchiseId`) 조회 시 period는 무조건 `MONTHLY`만 허용  
            - 기본 기준일은 전달 1일이며, 필요 시 `targetDate` 지정 가능
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "재고 회전율 통계 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<List<InventoryTurnoverResponse>>> getInventoryTurnover(
            @Parameter(description = "조회 주기 (WEEKLY 또는 MONTHLY)", example = "MONTHLY")
            @RequestParam(defaultValue = "MONTHLY") String period,

            @Parameter(description = "가맹점 ID (해당 값이 있을 경우 해당 가맹점 회전율만 조회됨)", example = "3")
            @RequestParam(required = false) Long franchiseId,

            @Parameter(description = "기준 날짜 (기본값: 전달 1일)", example = "2025-06-01")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                inventoryStatisticsQueryService.getTurnover(period, franchiseId, targetDate)
        ));
    }

    @Operation(summary = "재고 회전율 추이 조회")
    @GetMapping("/trend")
    public ResponseEntity<ApiResponse<List<InventoryTurnoverTrendResponse>>> getInventoryTurnoverTrend(
            @RequestParam(defaultValue = "MONTHLY") String period,
            @RequestParam(required = false) Long franchiseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        List<InventoryTurnoverTrendResponse> result =
                inventoryStatisticsQueryService.getTrend(period.toUpperCase(), franchiseId, targetDate);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

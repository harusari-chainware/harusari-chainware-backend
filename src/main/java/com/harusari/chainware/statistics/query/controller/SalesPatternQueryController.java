package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.service.salesPattern.SalesPatternQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statistics/patterns")
@RequiredArgsConstructor
@Tag(name = "매출 패턴 통계 API", description = "본사 또는 가맹점의 시간대별 / 요일별 / 일자별 매출 패턴을 조회하는 API입니다.")
public class SalesPatternQueryController {

    private final SalesPatternQueryService salesPatternQueryService;

    @Operation(
            summary = "매출 패턴 조회",
            description = """
            본사 또는 가맹점의 매출 패턴을 조회합니다.  
            - `HOURLY`: 시간대별 매출  
            - `WEEKLY`: 요일별 매출  
            - `MONTHLY`: 일자별 매출  
            - `franchiseId`가 없으면 본사 기준, 있으면 해당 가맹점 기준으로 조회됩니다.  
            - `targetDate`가 없으면 기본값은 어제입니다.
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "매출 패턴 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getSalesPattern(
            @Parameter(description = "조회 단위 (HOURLY, WEEKLY, MONTHLY)", example = "HOURLY")
            @RequestParam(defaultValue = "HOURLY") String period,

            @Parameter(description = "가맹점 ID (없으면 본사 기준)", example = "3")
            @RequestParam(required = false) Long franchiseId,

            @Parameter(description = "기준일 (기본값: 어제)", example = "2025-06-30")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return ResponseEntity.ok(ApiResponse.success(
                salesPatternQueryService.getSalesPattern(period, franchiseId, targetDate)
        ));
    }
}

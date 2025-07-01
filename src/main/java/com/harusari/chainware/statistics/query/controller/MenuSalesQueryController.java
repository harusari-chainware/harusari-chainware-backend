package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.menuSales.MenuSalesResponse;
import com.harusari.chainware.statistics.query.service.menuSales.MenuSalesQueryService;
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
@RequestMapping("/api/v1/statistics/menu-sales")
@RequiredArgsConstructor
@Tag(name = "메뉴 매출 통계 API", description = "메뉴별 매출 통계를 조회하는 API입니다.")
public class MenuSalesQueryController {

    private final MenuSalesQueryService menuSalesQueryService;

    @Operation(
            summary = "메뉴 매출 통계 조회",
            description = """
            메뉴별 매출 통계를 조회합니다.  
            - 가맹점 ID가 있는 경우 해당 가맹점의 매출 통계 조회  
            - 가맹점 ID가 없으면 본사 전체 기준으로 조회  
            - 기간 단위는 `DAILY`, `WEEKLY`, `MONTHLY` 중 하나  
            - 기준일이 없으면 기본값은 어제 날짜입니다.
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "메뉴 매출 통계 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MenuSalesResponse>>> getMenuSales(
            @Parameter(description = "가맹점 ID (없으면 본사 전체 기준 조회)", example = "3")
            @RequestParam(required = false) Long franchiseId,

            @Parameter(description = "조회 단위 (DAILY, WEEKLY, MONTHLY)", example = "DAILY")
            @RequestParam(defaultValue = "DAILY") String periodType,

            @Parameter(description = "기준일 (기본값: 어제)", example = "2025-06-30")
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

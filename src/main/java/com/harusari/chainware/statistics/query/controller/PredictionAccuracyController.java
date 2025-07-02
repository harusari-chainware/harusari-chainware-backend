package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.service.predictionAccuracy.PredictionAccuracyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/accuracy")
@RequiredArgsConstructor
@Tag(name = "예측 정확도 통계 API", description = "매출/주문/발주 예측 정확도(MAE, RMSE, MAPE)를 조회하는 API입니다.")
public class PredictionAccuracyController {

    private final PredictionAccuracyService service;

    @Operation(
            summary = "예측 정확도 요약 조회",
            description = """
            예측 정확도(MAE, RMSE, MAPE)를 요약 조회합니다.  
            - predictionType: sales(매출), order(주문), purchase(발주) 중 하나  
            - periodType: DAILY → 단일 결과, WEEKLY/MONTHLY → 리스트 반환  
            - franchiseId 없으면 전체 대상 정확도, 있으면 해당 가맹점만 조회  
            - targetDate 기준으로 통계 계산 (기본값: 어제)
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "정확도 통계 조회 성공")
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<?>> getSummary(
            @Parameter(description = "예측 유형 (sales, order, purchase)", example = "sales")
            @RequestParam(defaultValue = "sales") String predictionType,

            @Parameter(description = "조회 단위 (DAILY, WEEKLY, MONTHLY)", example = "WEEKLY")
            @RequestParam(defaultValue = "DAILY") String periodType,

            @Parameter(description = "기준일 (기본값: 어제)", example = "2025-06-30")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,

            @Parameter(description = "가맹점 ID (없으면 전체 대상 정확도 조회)", example = "5")
            @RequestParam(required = false) Long franchiseId
    ) {
        if (targetDate == null) {
            targetDate = LocalDate.now().minusDays(1);
        }

        return ResponseEntity.ok(ApiResponse.success(
                service.getSummary(predictionType, periodType, targetDate, franchiseId)
        ));
    }
}

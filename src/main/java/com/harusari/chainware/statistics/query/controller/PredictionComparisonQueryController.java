package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.predictionComparison.PredictionComparisonDto;
import com.harusari.chainware.statistics.query.service.predictionComparison.PredictionComparisonQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
@Tag(name = "예측 비교 통계 API", description = "실제 실적과 예측값을 비교하는 통계 API입니다.")
public class PredictionComparisonQueryController {

    private final PredictionComparisonQueryService service;

    @Operation(
            summary = "예측 vs 실적 비교 조회",
            description = """
            예측값과 실제 실적 데이터를 비교하여 조회합니다.  
            - 지난주 월~일의 **실적**과 이번 주 월~일의 **예측** 데이터를 함께 반환합니다.  
            - predictionType은 다음 중 하나입니다:  
              - `sales`: 매출  
              - `order_quantity`: 주문 수량  
              - `purchase_quantity`: 발주 수량  
            - franchiseId를 지정하면 해당 가맹점 기준으로, 없으면 전체 기준으로 조회됩니다.
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "예측 비교 데이터 조회 성공")
    @GetMapping("/prediction-comparison")
    public ResponseEntity<ApiResponse<List<PredictionComparisonDto>>> getPredictionComparison(
            @Parameter(description = "예측 유형 (sales, order_quantity, purchase_quantity)", example = "sales")
            @RequestParam(defaultValue = "sales") String predictionType,

            @Parameter(description = "가맹점 ID (지정 시 해당 가맹점 기준 조회)", example = "5")
            @RequestParam(required = false) Long franchiseId
    ) {
        List<PredictionComparisonDto> result = service.getPredictionComparison(predictionType, franchiseId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

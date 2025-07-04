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
@Tag(name = "예측 비교 통계 API", description = "실제 실적과 예측값을 조회하는 통계 API입니다.")
public class PredictionComparisonQueryController {

    private final PredictionComparisonQueryService service;

    @Operation(summary = "예측 vs 실적 비교 조회")
    @GetMapping("/prediction-comparison")
    public ResponseEntity<ApiResponse<List<PredictionComparisonDto>>> getPredictionComparison(
            @RequestParam(defaultValue = "sales") String predictionType,
            @RequestParam(required = false) Long franchiseId
    ) {
        List<PredictionComparisonDto> result = service.getPredictionComparison(predictionType, franchiseId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}


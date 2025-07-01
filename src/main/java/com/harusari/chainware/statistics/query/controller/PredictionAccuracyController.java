package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.statistics.query.dto.predictionAccuracy.PredictionAccuracyResponseDto;
import com.harusari.chainware.statistics.query.service.predictionAccuracy.PredictionAccuracyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/accuracy")
@RequiredArgsConstructor
public class PredictionAccuracyController {

    private final PredictionAccuracyService service;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<?>> getSummary(
            @RequestParam(defaultValue = "sales") String predictionType,
            @RequestParam(defaultValue = "DAILY") String periodType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate,
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

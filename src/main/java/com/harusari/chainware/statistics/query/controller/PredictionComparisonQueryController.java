package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.predictionComparison.PredictionComparisonDto;
import com.harusari.chainware.statistics.query.service.predictionComparison.PredictionComparisonQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class PredictionComparisonQueryController {

    private final PredictionComparisonQueryService service;

    @GetMapping("/prediction-comparison")
    public List<PredictionComparisonDto> getPredictionComparison(
            @RequestParam(defaultValue = "sales") String predictionType,
            @RequestParam(required = false) Long franchiseId
    ) {
        return service.getPredictionComparison(predictionType, franchiseId);
    }
}
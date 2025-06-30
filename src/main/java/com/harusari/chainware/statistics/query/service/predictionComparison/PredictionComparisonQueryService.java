package com.harusari.chainware.statistics.query.service.predictionComparison;

import com.harusari.chainware.statistics.query.dto.predictionComparison.PredictionComparisonDto;

import java.util.List;

public interface PredictionComparisonQueryService {

    List<PredictionComparisonDto> getPredictionComparison(String predictionType, Long franchiseId);

}
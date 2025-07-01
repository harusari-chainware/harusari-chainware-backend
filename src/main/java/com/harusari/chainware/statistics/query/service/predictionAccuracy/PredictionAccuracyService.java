package com.harusari.chainware.statistics.query.service.predictionAccuracy;


import com.harusari.chainware.statistics.query.dto.predictionAccuracy.PredictionAccuracyResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface PredictionAccuracyService {

    Object getSummary(String predictionType, String periodType, LocalDate targetDate, Long franchiseId);
}

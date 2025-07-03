package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.predictionAccuracy.PredictionAccuracyResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PredictionAccuracyQueryMapper {

    PredictionAccuracyResponseDto findDailyAccuracyByFranchise(
            @Param("type") String type,
            @Param("targetDate") LocalDate targetDate,
            @Param("franchiseId") Long franchiseId
    );

    PredictionAccuracyResponseDto findDailyAccuracyByType(
            @Param("type") String type,
            @Param("targetDate") LocalDate targetDate
    );

    List<PredictionAccuracyResponseDto> findWeeklyAccuracyByFranchise
            (@Param("type") String type,
             @Param("startDate") LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("franchiseId") Long franchiseId
            );

    List<PredictionAccuracyResponseDto> findWeeklyAccuracyByType(
            @Param("type") String type,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}

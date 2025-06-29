package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.predictionComparison.PredictionComparisonDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PredictionComparisonQueryMapper {

    List<PredictionComparisonDto> getActualSales(
            @Param("startDate") LocalDate start,
            @Param("endDate") LocalDate end,
            @Param("franchiseId") Long franchiseId
    );
    List<PredictionComparisonDto> getPredictedSales(
            @Param("startDate") LocalDate start,
            @Param("endDate") LocalDate end,
            @Param("franchiseId") Long franchiseId
    );


    List<PredictionComparisonDto> getActualOrderQuantity(
            @Param("startDate") LocalDate start,
            @Param("endDate") LocalDate end,
            @Param("franchiseId") Long franchiseId
    );
    List<PredictionComparisonDto> getPredictedOrderQuantity(
            @Param("startDate") LocalDate start,
            @Param("endDate") LocalDate end,
            @Param("franchiseId") Long franchiseId
    );


    List<PredictionComparisonDto> getActualPurchaseQuantity(
            @Param("startDate") LocalDate start,
            @Param("endDate") LocalDate end
    );
    List<PredictionComparisonDto> getPredictedPurchaseQuantity(
            @Param("startDate") LocalDate start,
            @Param("endDate") LocalDate end
    );
}

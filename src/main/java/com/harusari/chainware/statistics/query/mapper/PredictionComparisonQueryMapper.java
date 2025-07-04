package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.predictionComparison.PredictionComparisonDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface PredictionComparisonQueryMapper {

    // SALES
    List<PredictionComparisonDto> getActualSales(LocalDate startDate, LocalDate endDate, Long franchiseId);
    List<PredictionComparisonDto> getPredictedSales(LocalDate startDate, LocalDate endDate, Long franchiseId);
    List<PredictionComparisonDto> getActualSalesAllFranchises(LocalDate startDate, LocalDate endDate);
    List<PredictionComparisonDto> getPredictedSalesAllFranchises(LocalDate startDate, LocalDate endDate);

    // ORDER
    List<PredictionComparisonDto> getActualOrderQuantity(LocalDate startDate, LocalDate endDate, Long franchiseId);
    List<PredictionComparisonDto> getPredictedOrderQuantity(LocalDate startDate, LocalDate endDate, Long franchiseId);
    List<PredictionComparisonDto> getActualOrderQuantityAllFranchises(LocalDate startDate, LocalDate endDate);
    List<PredictionComparisonDto> getPredictedOrderQuantityAllFranchises(LocalDate startDate, LocalDate endDate);

    // PURCHASE
    List<PredictionComparisonDto> getActualPurchaseQuantity(LocalDate startDate, LocalDate endDate);
    List<PredictionComparisonDto> getPredictedPurchaseQuantity(LocalDate startDate, LocalDate endDate);
}

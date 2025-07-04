package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateProductStatisticsResponse;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface DisposalRateStatisticsQueryMapper {

    List<DisposalRateStatisticsResponse> getDisposalRate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseId") Long warehouseId,
            @Param("franchiseId") Long franchiseId
    );

    List<DisposalRateProductStatisticsResponse> getProductLevelDisposalRate(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseId") Long warehouseId,
            @Param("franchiseId") Long franchiseId
    );

    List<DisposalRateStatisticsResponse> getTrendForTotal(
            @Param("period") String period,
            @Param("targetDate") LocalDate targetDate
    );

    List<DisposalRateStatisticsResponse> getTrendForHeadquarters(
            @Param("period") String period,
            @Param("targetDate") LocalDate targetDate
            );

    List<DisposalRateStatisticsResponse> getTrendForFranchises(
            @Param("period") String period,
            @Param("targetDate") LocalDate targetDate
    );
}

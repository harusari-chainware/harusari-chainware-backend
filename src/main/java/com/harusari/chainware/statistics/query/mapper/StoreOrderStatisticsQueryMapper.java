package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.StoreOrderProductStatisticsResponse;
import com.harusari.chainware.statistics.query.dto.StoreOrderStatisticsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StoreOrderStatisticsQueryMapper {

    List<StoreOrderStatisticsResponse> getFranchiseLevelStatistics(
            @Param("franchiseId") Long franchiseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<StoreOrderProductStatisticsResponse> getProductLevelStatistics(
            @Param("franchiseId") Long franchiseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}

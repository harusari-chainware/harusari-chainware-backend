package com.harusari.chainware.statistics.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface TotalSalesStatisticsQueryMapper {

    Long getTotalAmount(
            @Param("franchiseId") Long franchiseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.InventoryTurnoverResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InventoryStatisticsMapper {

    // 본사 주간 회전율
    List<InventoryTurnoverResponse> getWeeklyTurnover(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 본사 월간 회전율
    List<InventoryTurnoverResponse> getMonthlyTurnover(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 가맹점 월간 회전율
    List<InventoryTurnoverResponse> getFranchiseMonthlyTurnover(
            @Param("franchiseId") Long franchiseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.InventoryTurnoverResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InventoryStatisticsMapper {

    List<InventoryTurnoverResponse> selectWarehouseTurnoverMonthly(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<InventoryTurnoverResponse> selectWarehouseTurnoverWeekly(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<InventoryTurnoverResponse> selectFranchiseTurnoverWithBom(
            @Param("franchiseId") Long franchiseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}

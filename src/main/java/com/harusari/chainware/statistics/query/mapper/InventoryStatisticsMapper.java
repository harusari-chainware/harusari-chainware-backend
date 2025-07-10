package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.dto.inventoryTurnover.InventoryTurnoverTrendResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface InventoryStatisticsMapper {

    List<InventoryTurnoverResponse> getDailyTurnover(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<InventoryTurnoverResponse> getWeeklyTurnover(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<InventoryTurnoverResponse> getMonthlyTurnover(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<InventoryTurnoverResponse> getFranchiseMonthlyTurnover(
            @Param("franchiseId") Long franchiseId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<InventoryTurnoverTrendResponse> getMonthlyTurnoverTrend(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("franchiseId") Long franchiseId
    );
    List<InventoryTurnoverTrendResponse> getWeeklyTurnoverTrend(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("franchiseId") Long franchiseId
    );
    List<InventoryTurnoverTrendResponse> getDailyTurnoverTrend(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("franchiseId") Long franchiseId
    );

    List<InventoryTurnoverTrendResponse> getFranchiseMonthlyTurnoverTrend(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("franchiseId") Long franchiseId
    );

    List<InventoryTurnoverResponse> getDailyTurnoverByWarehouse(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseId") Long warehouseId
    );

    List<InventoryTurnoverTrendResponse> getWeeklyTurnoverTrendByWarehouse(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseId") Long warehouseId
    );

    List<InventoryTurnoverTrendResponse> getMonthlyTurnoverTrendByWarehouse(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseId") Long warehouseId
    );

    List<InventoryTurnoverResponse> getWeeklyTurnoverByWarehouse(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseId") Long warehouseId
    );

    List<InventoryTurnoverResponse> getMonthlyTurnoverByWarehouse(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("warehouseId") Long warehouseId
    );

}

package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.DailySalesResponse;
import com.harusari.chainware.statistics.query.dto.HourlySalesResponse;
import com.harusari.chainware.statistics.query.dto.WeekdaySalesResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SalesPatternMapper {

    List<HourlySalesResponse> selectHourlySalesByFranchise(
            @Param("franchiseId") Long franchiseId,
            @Param("targetDate") LocalDate targetDate
    );
    List<HourlySalesResponse> selectHourlySalesForHeadquarters(
            @Param("targetDate") LocalDate targetDate
    );


    List<WeekdaySalesResponse> selectWeekdaySalesByFranchise(
            @Param("franchiseId") Long franchiseId,
            @Param("targetDate") LocalDate targetDate
    );
    List<WeekdaySalesResponse> selectWeekdaySalesForHeadquarters(
            @Param("targetDate") LocalDate targetDate
    );


    List<DailySalesResponse> selectDailySalesByFranchise(
            @Param("franchiseId") Long franchiseId,
            @Param("targetDate") LocalDate targetDate
    );
    List<DailySalesResponse> selectDailySalesForHeadquarters(
            @Param("targetDate") LocalDate targetDate
    );
}
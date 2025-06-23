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

    // 시간대별
    List<HourlySalesResponse> getHourlySalesForHeadquarters(
            @Param("date") LocalDate date);
    List<HourlySalesResponse> getHourlySalesByFranchise(
            @Param("franchiseId") Long franchiseId, @Param("date") LocalDate date);

    // 요일별 평균 (7일)
    List<WeekdaySalesResponse> getWeekdaySalesForHeadquarters(
            @Param("baseDate") LocalDate baseDate);
    List<WeekdaySalesResponse> getWeekdaySalesByFranchise(
            @Param("franchiseId") Long franchiseId, @Param("baseDate") LocalDate baseDate);

    // 일별 추이 (30일)
    List<DailySalesResponse> getDailySalesForHeadquarters(
            @Param("baseDate") LocalDate baseDate);
    List<DailySalesResponse> getDailySalesByFranchise(
            @Param("franchiseId") Long franchiseId, @Param("baseDate") LocalDate baseDate);
}
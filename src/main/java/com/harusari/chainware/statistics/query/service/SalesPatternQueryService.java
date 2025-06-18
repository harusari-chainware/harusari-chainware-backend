package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.DailySalesResponse;
import com.harusari.chainware.statistics.query.dto.HourlySalesResponse;
import com.harusari.chainware.statistics.query.dto.WeekdaySalesResponse;

import java.time.LocalDate;
import java.util.List;

public interface SalesPatternQueryService {
    List<HourlySalesResponse> getHourlySalesByFranchise(Long franchiseId, LocalDate targetDate);
    List<HourlySalesResponse> getHourlySalesForHeadquarters(LocalDate targetDate);

    List<WeekdaySalesResponse> getWeekdaySalesByFranchise(Long franchiseId, LocalDate targetDate);
    List<WeekdaySalesResponse> getWeekdaySalesForHeadquarters(LocalDate targetDate);

    List<DailySalesResponse> getDailySalesByFranchise(Long franchiseId, LocalDate targetDate);
    List<DailySalesResponse> getDailySalesForHeadquarters(LocalDate targetDate);
}
package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.HourlySalesResponse;

import java.time.LocalDate;
import java.util.List;

public interface SalesPatternQueryService {
    List<HourlySalesResponse> getHourlySalesByFranchise(Long franchiseId, LocalDate targetDate);
}
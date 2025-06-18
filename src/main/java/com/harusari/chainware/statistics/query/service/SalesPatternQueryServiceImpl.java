package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.DailySalesResponse;
import com.harusari.chainware.statistics.query.dto.HourlySalesResponse;
import com.harusari.chainware.statistics.query.dto.WeekdaySalesResponse;
import com.harusari.chainware.statistics.query.mapper.SalesPatternMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesPatternQueryServiceImpl implements SalesPatternQueryService {

    private final SalesPatternMapper salesPatternMapper;

    @Override
    @Transactional
    public List<HourlySalesResponse> getHourlySalesByFranchise(Long franchiseId, LocalDate targetDate) {
        return salesPatternMapper.selectHourlySalesByFranchise(franchiseId, targetDate);
    }

    @Override
    @Transactional
    public List<HourlySalesResponse> getHourlySalesForHeadquarters(LocalDate targetDate) {
        return salesPatternMapper.selectHourlySalesForHeadquarters(targetDate);
    }

    @Override
    @Transactional
    public List<WeekdaySalesResponse> getWeekdaySalesByFranchise(Long franchiseId, LocalDate targetDate) {
        return salesPatternMapper.selectWeekdaySalesByFranchise(franchiseId, targetDate);
    }

    @Override
    @Transactional
    public List<WeekdaySalesResponse> getWeekdaySalesForHeadquarters(LocalDate targetDate) {
        return salesPatternMapper.selectWeekdaySalesForHeadquarters(targetDate);
    }

    @Override
    @Transactional
    public List<DailySalesResponse> getDailySalesByFranchise(Long franchiseId, LocalDate targetDate) {
        return salesPatternMapper.selectDailySalesByFranchise(franchiseId, targetDate);
    }

    @Override
    @Transactional
    public List<DailySalesResponse> getDailySalesForHeadquarters(LocalDate targetDate) {
        return salesPatternMapper.selectDailySalesForHeadquarters(targetDate);
    }
}
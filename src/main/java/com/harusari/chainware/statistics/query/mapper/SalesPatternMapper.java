package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.HourlySalesResponse;
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
}
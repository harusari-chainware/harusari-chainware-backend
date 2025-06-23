package com.harusari.chainware.statistics.query.mapper;

import com.harusari.chainware.statistics.query.dto.menuSales.MenuSalesResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface MenuSalesMapper {
    List<MenuSalesResponse> selectMenuSalesByPeriod(
            @Param("franchiseId") Long franchiseId,
            @Param("periodType") String periodType,
            @Param("targetDate") LocalDate targetDate
    );

    List<MenuSalesResponse> selectMenuSalesForHeadquarters(
            @Param("periodType") String periodType,
            @Param("targetDate") LocalDate targetDate
    );
}

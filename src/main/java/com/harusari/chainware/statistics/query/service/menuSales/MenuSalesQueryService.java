package com.harusari.chainware.statistics.query.service.menuSales;

import com.harusari.chainware.statistics.query.dto.menuSales.MenuSalesResponse;

import java.time.LocalDate;
import java.util.List;

public interface MenuSalesQueryService {
    List<MenuSalesResponse> getMenuSalesByPeriod(Long franchiseId, String periodType, LocalDate targetDate);
    List<MenuSalesResponse> getMenuSalesForHeadquarters(String periodType, LocalDate targetDate);

}

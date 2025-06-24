package com.harusari.chainware.statistics.query.service.menuSales;

import com.harusari.chainware.statistics.query.dto.menuSales.MenuSalesResponse;
import com.harusari.chainware.statistics.query.mapper.MenuSalesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuSalesQueryServiceImpl implements MenuSalesQueryService {

    private final MenuSalesMapper menuSalesMapper;

    @Override
    @Transactional
    public List<MenuSalesResponse> getMenuSalesByPeriod(Long franchiseId, String periodType, LocalDate targetDate) {
        return menuSalesMapper.selectMenuSalesByPeriod(franchiseId, periodType, targetDate);
    }

    @Override
    @Transactional
    public List<MenuSalesResponse> getMenuSalesForHeadquarters(String periodType, LocalDate targetDate) {
        return menuSalesMapper.selectMenuSalesForHeadquarters(periodType, targetDate);
    }
}

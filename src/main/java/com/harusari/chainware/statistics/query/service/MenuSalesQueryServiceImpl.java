package com.harusari.chainware.statistics.query.service;

import com.harusari.chainware.statistics.query.dto.MenuSalesResponse;
import com.harusari.chainware.statistics.query.mapper.MenuSalesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuSalesQueryServiceImpl implements MenuSalesQueryService {

    private final MenuSalesMapper menuSalesMapper;

    @Override
    public List<MenuSalesResponse> getMenuSalesByPeriod(Long franchiseId, String periodType, LocalDate targetDate) {
        return menuSalesMapper.selectMenuSalesByPeriod(franchiseId, periodType, targetDate);
    }
}

package com.harusari.chainware.statistics.query.service.menuSales;

import com.harusari.chainware.statistics.query.dto.menuSales.MenuSalesResponse;
import com.harusari.chainware.statistics.query.mapper.MenuSalesMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("[통계 - 메뉴 매출] MenuSalesQueryServiceImpl 테스트")
class MenuSalesQueryServiceImplTest {

    @Mock
    private MenuSalesMapper mapper;

    @InjectMocks
    private MenuSalesQueryServiceImpl service;

    public MenuSalesQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("가맹점 매출 조회")
    class FranchiseMenuSales {

        @Test
        @DisplayName("1. 가맹점 메뉴 매출 조회 정상 처리")
        void testGetMenuSalesByPeriod() {
            // given
            Long franchiseId = 100L;
            String periodType = "DAILY";
            LocalDate date = LocalDate.of(2025, 6, 20);

            when(mapper.selectMenuSalesByPeriod(franchiseId, periodType, date))
                    .thenReturn(List.of());

            // when
            List<MenuSalesResponse> result = service.getMenuSalesByPeriod(franchiseId, periodType, date);

            // then
            assertThat(result).isEmpty();
            verify(mapper).selectMenuSalesByPeriod(franchiseId, periodType, date);
        }
    }

    @Nested
    @DisplayName("본사 매출 조회")
    class HeadquartersMenuSales {

        @Test
        @DisplayName("2. 본사 메뉴 매출 조회 정상 처리")
        void testGetMenuSalesForHeadquarters() {
            // given
            String periodType = "WEEKLY";
            LocalDate date = LocalDate.of(2025, 6, 21);

            when(mapper.selectMenuSalesForHeadquarters(periodType, date))
                    .thenReturn(List.of());

            // when
            List<MenuSalesResponse> result = service.getMenuSalesForHeadquarters(periodType, date);

            // then
            assertThat(result).isEmpty();
            verify(mapper).selectMenuSalesForHeadquarters(periodType, date);
        }
    }
}

package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.menuSales.MenuSalesResponse;
import com.harusari.chainware.statistics.query.service.menuSales.MenuSalesQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuSalesQueryController.class)
@DisplayName("[통계 - 메뉴 매출] MenuSalesQueryController 테스트")
class MenuSalesQueryControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private MenuSalesQueryService service;

    private final MenuSalesResponse dummy = new MenuSalesResponse();

    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @Nested
    @DisplayName("파라미터 조합 테스트")
    class ParameterCombinations {

        @Test
        @DisplayName("1. 기본값만 사용 (본사 + DAILY + 어제)")
        void testDefault() throws Exception {
            LocalDate defaultDate = LocalDate.now().minusDays(1);
            doReturn(List.of(dummy))
                    .when(service).getMenuSalesForHeadquarters(eq("DAILY"), eq(defaultDate));

            mockMvc.perform(get("/api/v1/statistics/menu-sales"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("2. 본사 + WEEKLY + targetDate 지정")
        void testHeadquartersWithPeriodAndDate() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 10);
            doReturn(List.of(dummy))
                    .when(service).getMenuSalesForHeadquarters(eq("WEEKLY"), eq(date));

            mockMvc.perform(get("/api/v1/statistics/menu-sales")
                            .param("periodType", "WEEKLY")
                            .param("targetDate", "2025-06-10"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("3. 가맹점 ID만 지정")
        void testFranchiseOnly() throws Exception {
            LocalDate defaultDate = LocalDate.now().minusDays(1);
            doReturn(List.of(dummy))
                    .when(service).getMenuSalesByPeriod(eq(100L), eq("DAILY"), eq(defaultDate));

            mockMvc.perform(get("/api/v1/statistics/menu-sales")
                            .param("franchiseId", "100"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("4. 가맹점 + periodType 지정")
        void testFranchiseWithPeriod() throws Exception {
            LocalDate defaultDate = LocalDate.now().minusDays(1);
            doReturn(List.of(dummy))
                    .when(service).getMenuSalesByPeriod(eq(100L), eq("MONTHLY"), eq(defaultDate));

            mockMvc.perform(get("/api/v1/statistics/menu-sales")
                            .param("franchiseId", "100")
                            .param("periodType", "MONTHLY"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("5. 가맹점 + 전체 파라미터")
        void testFranchiseAllParams() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 9);
            doReturn(List.of(dummy))
                    .when(service).getMenuSalesByPeriod(eq(100L), eq("WEEKLY"), eq(date));

            mockMvc.perform(get("/api/v1/statistics/menu-sales")
                            .param("franchiseId", "100")
                            .param("periodType", "WEEKLY")
                            .param("targetDate", "2025-06-09"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

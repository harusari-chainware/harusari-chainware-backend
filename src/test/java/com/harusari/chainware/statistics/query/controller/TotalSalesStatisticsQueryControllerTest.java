package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.totalSales.TotalSalesStatisticsResponse;
import com.harusari.chainware.statistics.query.service.totalSales.TotalSalesStatisticsQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TotalSalesStatisticsQueryController.class)
@DisplayName("[통계 - 총 매출] TotalSalesStatisticsQueryController 테스트")
class TotalSalesStatisticsQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private TotalSalesStatisticsQueryService service;

    private final TotalSalesStatisticsResponse dummy = new TotalSalesStatisticsResponse();

    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @Nested
    @DisplayName("모든 파라미터 조합 테스트")
    class ParameterCombinationTests {

        @Test
        @DisplayName("1. 기본값 조회 (period=DAILY, 나머지 없음)")
        void testDefault() throws Exception {
            doReturn(dummy).when(service)
                    .getStatistics(eq("DAILY"), isNull(), isNull());

            mockMvc.perform(get("/api/v1/statistics/total-sales"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("2. DAILY + franchiseId")
        void testDailyWithFranchise() throws Exception {
            doReturn(dummy).when(service)
                    .getStatistics(eq("DAILY"), eq(101L), isNull());

            mockMvc.perform(get("/api/v1/statistics/total-sales")
                            .param("period", "DAILY")
                            .param("franchiseId", "101"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("3. WEEKLY + targetDate")
        void testWeeklyWithDate() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 28);
            doReturn(dummy).when(service)
                    .getStatistics(eq("WEEKLY"), isNull(), eq(date));

            mockMvc.perform(get("/api/v1/statistics/total-sales")
                            .param("period", "WEEKLY")
                            .param("targetDate", "2025-06-28"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("4. MONTHLY + franchiseId + targetDate")
        void testMonthlyFullParams() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 1);
            doReturn(dummy).when(service)
                    .getStatistics(eq("MONTHLY"), eq(202L), eq(date));

            mockMvc.perform(get("/api/v1/statistics/total-sales")
                            .param("period", "MONTHLY")
                            .param("franchiseId", "202")
                            .param("targetDate", "2025-06-01"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

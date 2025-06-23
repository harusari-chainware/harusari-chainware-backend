package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderStatisticsResponse;
import com.harusari.chainware.statistics.query.service.storeOrder.StoreOrderStatisticsQueryService;
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

@WebMvcTest(StoreOrderStatisticsQueryController.class)
@DisplayName("[통계 - 가맹점 발주량] StoreOrderStatisticsQueryController 테스트")
class StoreOrderStatisticsQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private StoreOrderStatisticsQueryService service;

    private final StoreOrderStatisticsResponse dummy = new StoreOrderStatisticsResponse();

    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @Nested
    @DisplayName("모든 파라미터 조합 테스트")
    class ParameterCombinationTests {

        @Test
        @DisplayName("1. period=DAILY만 전달")
        void testDailyOnly() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("DAILY"), isNull(), isNull(), eq(false));

            mockMvc.perform(get("/api/v1/statistics/store-order")
                            .param("period", "DAILY"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("2. period=WEEKLY + franchiseId")
        void testWeeklyWithFranchiseId() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("WEEKLY"), eq(100L), isNull(), eq(false));

            mockMvc.perform(get("/api/v1/statistics/store-order")
                            .param("period", "WEEKLY")
                            .param("franchiseId", "100"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("3. period=MONTHLY + targetDate")
        void testMonthlyWithTargetDate() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 25);
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("MONTHLY"), isNull(), eq(date), eq(false));

            mockMvc.perform(get("/api/v1/statistics/store-order")
                            .param("period", "MONTHLY")
                            .param("targetDate", "2025-06-25"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("4. 전체 파라미터 (DAILY + franchiseId + targetDate + includeProduct=true)")
        void testAllParameters() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 15);
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("DAILY"), eq(200L), eq(date), eq(true));

            mockMvc.perform(get("/api/v1/statistics/store-order")
                            .param("period", "DAILY")
                            .param("franchiseId", "200")
                            .param("targetDate", "2025-06-15")
                            .param("includeProduct", "true"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("5. period=WEEKLY + includeProduct=true")
        void testWeeklyWithProductOnly() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("WEEKLY"), isNull(), isNull(), eq(true));

            mockMvc.perform(get("/api/v1/statistics/store-order")
                            .param("period", "WEEKLY")
                            .param("includeProduct", "true"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

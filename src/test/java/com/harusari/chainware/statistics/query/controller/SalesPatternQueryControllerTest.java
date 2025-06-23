package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.service.salesPattern.SalesPatternQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalesPatternQueryController.class)
@DisplayName("[통계 - 판매 패턴] SalesPatternQueryController 테스트")
class SalesPatternQueryControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private SalesPatternQueryService service;

    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @Nested
    @DisplayName("모든 파라미터 조합 테스트")
    class ParameterCombinationTests {

        @Test
        @DisplayName("1. 기본값 (period=HOURLY, franchiseId 없음, targetDate 없음)")
        void testDefault() throws Exception {
            doReturn(Map.of("result", "success"))
                    .when(service).getSalesPattern(eq("HOURLY"), isNull(), any());

            mockMvc.perform(get("/api/v1/statistics/patterns"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("2. period=HOURLY + franchiseId만 전달")
        void testHourlyWithFranchise() throws Exception {
            doReturn(Map.of("result", "ok"))
                    .when(service).getSalesPattern(eq("HOURLY"), eq(1L), any());

            mockMvc.perform(get("/api/v1/statistics/patterns")
                            .param("period", "HOURLY")
                            .param("franchiseId", "1"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("3. period=WEEKLY + targetDate만 전달")
        void testWeeklyWithTargetDate() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 20);
            doReturn(Map.of("weekly", "data"))
                    .when(service).getSalesPattern(eq("WEEKLY"), isNull(), eq(date));

            mockMvc.perform(get("/api/v1/statistics/patterns")
                            .param("period", "WEEKLY")
                            .param("targetDate", "2025-06-20"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("4. period=MONTHLY + franchiseId + targetDate")
        void testMonthlyWithAllParams() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 10);
            doReturn(Map.of("monthly", "data"))
                    .when(service).getSalesPattern(eq("MONTHLY"), eq(10L), eq(date));

            mockMvc.perform(get("/api/v1/statistics/patterns")
                            .param("period", "MONTHLY")
                            .param("franchiseId", "10")
                            .param("targetDate", "2025-06-10"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("5. 잘못된 period 전달 시 서비스 처리 확인")
        void testInvalidPeriod() throws Exception {
            doReturn(Map.of("fallback", "default"))
                    .when(service).getSalesPattern(eq("INVALID"), any(), any());

            mockMvc.perform(get("/api/v1/statistics/patterns")
                            .param("period", "INVALID"))
                    .andDo(print())
                    .andExpect(status().isOk()); // 또는 .isBadRequest()로 처리하도록 구현된 경우 수정
        }
    }
}

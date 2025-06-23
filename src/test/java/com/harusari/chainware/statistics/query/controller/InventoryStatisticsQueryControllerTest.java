package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.invertoryTurnover.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.service.inventoryTurnover.InventoryStatisticsQueryService;
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

@WebMvcTest(InventoryStatisticsQueryController.class)
@DisplayName("[통계 - 재고회전율] InventoryStatisticsQueryController 테스트")
class InventoryStatisticsQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private InventoryStatisticsQueryService service;

    private final InventoryTurnoverResponse dummy = new InventoryTurnoverResponse();

    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @Nested
    @DisplayName("파라미터 조합 테스트")
    class ParameterCombinationTests {

        @Test
        @DisplayName("1. 모든 파라미터 없이 기본값 조회")
        void testDefaultParameters() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getTurnover(eq("MONTHLY"), isNull(), isNull());

            mockMvc.perform(get("/api/v1/statistics/inventory-turnover"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));
        }

        @Test
        @DisplayName("2. franchiseId만 있을 때")
        void testFranchiseOnly() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getTurnover(eq("MONTHLY"), eq(101L), isNull());

            mockMvc.perform(get("/api/v1/statistics/inventory-turnover")
                            .param("franchiseId", "101"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("3. targetDate만 있을 때")
        void testTargetDateOnly() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 10);
            doReturn(List.of(dummy)).when(service)
                    .getTurnover(eq("MONTHLY"), isNull(), eq(date));

            mockMvc.perform(get("/api/v1/statistics/inventory-turnover")
                            .param("targetDate", "2025-06-10"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("4. 모든 파라미터 조합")
        void testAllParameters() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 10);
            doReturn(List.of(dummy)).when(service)
                    .getTurnover(eq("WEEKLY"), eq(101L), eq(date));

            mockMvc.perform(get("/api/v1/statistics/inventory-turnover")
                            .param("period", "WEEKLY")
                            .param("franchiseId", "101")
                            .param("targetDate", "2025-06-10"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("5. 잘못된 period 값 전달 시")
        void testInvalidPeriod() throws Exception {
            // 서비스에서 예외를 던지도록 설정할 수도 있음
            doReturn(List.of(dummy)).when(service)
                    .getTurnover(eq("INVALID"), any(), any());

            mockMvc.perform(get("/api/v1/statistics/inventory-turnover")
                            .param("period", "INVALID"))
                    .andDo(print())
                    .andExpect(status().isOk()); // 또는 .isBadRequest()로 예외 처리 테스트 가능
        }
    }
}

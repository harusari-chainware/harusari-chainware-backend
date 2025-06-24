package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderStatisticsResponse;
import com.harusari.chainware.statistics.query.service.purchaseOrder.PurchaseOrderStatisticsQueryService;
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

@WebMvcTest(PurchaseOrderStatisticsQueryController.class)
@DisplayName("[통계 - 본사 발주량] PurchaseOrderStatisticsQueryController 테스트")
class PurchaseOrderStatisticsQueryControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private PurchaseOrderStatisticsQueryService service;

    private final PurchaseOrderStatisticsResponse dummy = new PurchaseOrderStatisticsResponse();

    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @Nested
    @DisplayName("모든 파라미터 조합 테스트")
    class ParameterCombinationTests {

        @Test
        @DisplayName("1. period=DAILY만 전달")
        void testDailyOnly() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("DAILY"), isNull(), isNull(), eq(false));

            mockMvc.perform(get("/api/v1/statistics/purchase-order")
                            .param("period", "DAILY"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("2. period=WEEKLY + targetDate")
        void testWeeklyWithTargetDate() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 20);
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("WEEKLY"), isNull(), eq(date), eq(false));

            mockMvc.perform(get("/api/v1/statistics/purchase-order")
                            .param("period", "WEEKLY")
                            .param("targetDate", "2025-06-20"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("3. period=MONTHLY + includeProduct=true")
        void testMonthlyWithProduct() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("MONTHLY"), isNull(), isNull(), eq(true));

            mockMvc.perform(get("/api/v1/statistics/purchase-order")
                            .param("period", "MONTHLY")
                            .param("includeProduct", "true"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("4. 전체 파라미터 전달")
        void testAllParams() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 15);
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("WEEKLY"), eq(501L), eq(date), eq(true));

            mockMvc.perform(get("/api/v1/statistics/purchase-order")
                            .param("period", "WEEKLY")
                            .param("vendorId", "501")
                            .param("targetDate", "2025-06-15")
                            .param("includeProduct", "true"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("5. vendorId만 있을 때")
        void testWithVendorOnly() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("DAILY"), eq(300L), isNull(), eq(false));

            mockMvc.perform(get("/api/v1/statistics/purchase-order")
                            .param("period", "DAILY")
                            .param("vendorId", "300"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("6. targetDate만 있을 때")
        void testWithTargetDateOnly() throws Exception {
            LocalDate date = LocalDate.of(2025, 6, 18);
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("DAILY"), isNull(), eq(date), eq(false));

            mockMvc.perform(get("/api/v1/statistics/purchase-order")
                            .param("period", "DAILY")
                            .param("targetDate", "2025-06-18"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("7. includeProduct=true만 있을 때")
        void testWithIncludeProductOnly() throws Exception {
            doReturn(List.of(dummy)).when(service)
                    .getStatistics(eq("DAILY"), isNull(), isNull(), eq(true));

            mockMvc.perform(get("/api/v1/statistics/purchase-order")
                            .param("period", "DAILY")
                            .param("includeProduct", "true"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

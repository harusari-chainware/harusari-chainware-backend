package com.harusari.chainware.statistics.query.controller;

import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponse;
import com.harusari.chainware.statistics.query.service.disposal.DisposalRateStatisticsQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisposalRateStatisticsQueryController.class)
@DisplayName("[통계 - 폐기율] DisposalRateStatisticsQueryController 테스트")
class DisposalRateStatisticsQueryControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private DisposalRateStatisticsQueryService service;

    private final DisposalRateStatisticsResponse dummy = new DisposalRateStatisticsResponse();

    @WithMockUser(username = "admin", roles = "GENERAL_MANAGER")
    @Nested
    @DisplayName("모든 파라미터 조합 테스트")
    class AllParameterCombinations {

        @Test
        @DisplayName("1. 가맹점 ID만 있을 때")
        void testDisposalRateWithFranchise() throws Exception {
            doReturn(List.of(dummy)).when(service).getDisposalStatistics(
                    eq("MONTHLY"), isNull(), eq(100L), eq(LocalDate.of(2025, 6, 1)), eq(false)
            );

            mockMvc.perform(get("/api/v1/statistics/disposal-rate")
                            .param("period", "MONTHLY")
                            .param("franchiseId", "100")
                            .param("targetDate", "2025-06-01")
                            .param("includeProduct", "false")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("2. 창고 ID만 있을 때")
        void testDisposalRateWithWarehouse() throws Exception {
            doReturn(List.of(dummy)).when(service).getDisposalStatistics(
                    eq("DAILY"), eq(200L), isNull(), eq(LocalDate.of(2025, 6, 2)), eq(false)
            );

            mockMvc.perform(get("/api/v1/statistics/disposal-rate")
                            .param("period", "DAILY")
                            .param("warehouseId", "200")
                            .param("targetDate", "2025-06-02")
                            .param("includeProduct", "false")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("3. 창고 ID와 가맹점 ID 모두 있을 때")
        void testDisposalRateWithBothWarehouseAndFranchise() throws Exception {
            doReturn(List.of(dummy)).when(service).getDisposalStatistics(
                    eq("WEEKLY"), eq(200L), eq(100L), eq(LocalDate.of(2025, 6, 3)), eq(false)
            );

            mockMvc.perform(get("/api/v1/statistics/disposal-rate")
                            .param("period", "WEEKLY")
                            .param("warehouseId", "200")
                            .param("franchiseId", "100")
                            .param("targetDate", "2025-06-03")
                            .param("includeProduct", "false")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("4. 본사 전체 폐기율 조회 (warehouseId, franchiseId 모두 없음)")
        void testDisposalRateHeadquartersAll() throws Exception {
            doReturn(List.of(dummy)).when(service).getDisposalStatistics(
                    eq("MONTHLY"), isNull(), isNull(), eq(LocalDate.of(2025, 6, 1)), eq(false)
            );

            mockMvc.perform(get("/api/v1/statistics/disposal-rate")
                            .param("period", "MONTHLY")
                            .param("targetDate", "2025-06-01")
                            .param("includeProduct", "false")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("5. includeProduct=true일 때 상세 제품별 폐기율 조회")
        void testDisposalRateWithIncludeProduct() throws Exception {
            doReturn(List.of(dummy)).when(service).getDisposalStatistics(
                    eq("WEEKLY"), isNull(), eq(100L), eq(LocalDate.of(2025, 6, 1)), eq(true)
            );

            mockMvc.perform(get("/api/v1/statistics/disposal-rate")
                            .param("period", "WEEKLY")
                            .param("franchiseId", "100")
                            .param("targetDate", "2025-06-01")
                            .param("includeProduct", "true")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("6. targetDate가 없을 때 기본값 처리 확인")
        void testDisposalRateWithNullTargetDate() throws Exception {
            // 기본 targetDate는 LocalDate.now().minusDays(1)으로 서비스 내부에서 처리됨
            doReturn(List.of(dummy)).when(service).getDisposalStatistics(
                    eq("DAILY"), isNull(), eq(100L), any(LocalDate.class), eq(false)
            );

            mockMvc.perform(get("/api/v1/statistics/disposal-rate")
                            .param("period", "DAILY")
                            .param("franchiseId", "100")
                            .param("includeProduct", "false")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }
}

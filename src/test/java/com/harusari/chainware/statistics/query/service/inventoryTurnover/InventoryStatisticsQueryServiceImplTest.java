package com.harusari.chainware.statistics.query.service.inventoryTurnover;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.invertoryTurnover.InventoryTurnoverResponse;
import com.harusari.chainware.statistics.query.mapper.InventoryStatisticsMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[통계 - 재고 회전율] InventoryStatisticsQueryServiceImpl 테스트")
class InventoryStatisticsQueryServiceImplTest {

    @Mock
    private InventoryStatisticsMapper mapper;

    @InjectMocks
    private InventoryStatisticsQueryServiceImpl service;

    public InventoryStatisticsQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("1. 본사 - WEEKLY 조회")
        void testWeeklyTurnover() {
            LocalDate baseDate = LocalDate.of(2025, 6, 17); // Tuesday

            when(mapper.getWeeklyTurnover(any(), any()))
                    .thenReturn(List.of());

            List<InventoryTurnoverResponse> result = service.getTurnover("WEEKLY", null, baseDate);

            assertThat(result).isEmpty();
            verify(mapper).getWeeklyTurnover(any(), any());
        }

        @Test
        @DisplayName("2. 본사 - MONTHLY 조회")
        void testMonthlyTurnover() {
            LocalDate baseDate = LocalDate.of(2025, 5, 1);

            when(mapper.getMonthlyTurnover(any(), any()))
                    .thenReturn(List.of());

            List<InventoryTurnoverResponse> result = service.getTurnover("MONTHLY", null, baseDate);

            assertThat(result).isEmpty();
            verify(mapper).getMonthlyTurnover(any(), any());
        }

        @Test
        @DisplayName("3. 가맹점 - MONTHLY 조회")
        void testFranchiseMonthlyTurnover() {
            LocalDate baseDate = LocalDate.of(2025, 4, 10);

            when(mapper.getFranchiseMonthlyTurnover(anyLong(), any(), any()))
                    .thenReturn(List.of());

            List<InventoryTurnoverResponse> result = service.getTurnover("MONTHLY", 100L, baseDate);

            assertThat(result).isEmpty();
            verify(mapper).getFranchiseMonthlyTurnover(eq(100L), any(), any());
        }
    }

    @Nested
    @DisplayName("예외 케이스")
    class ExceptionCases {

        @Test
        @DisplayName("4. 가맹점 - WEEKLY 조회 시 예외 발생")
        void testInvalidPeriodForFranchise() {
            LocalDate date = LocalDate.of(2025, 6, 1);

            assertThatThrownBy(() ->
                    service.getTurnover("WEEKLY", 1L, date))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.INVALID_PERIOD_FOR_FRANCHISE.getMessage());
        }

        @Test
        @DisplayName("5. 본사 - 지원하지 않는 period 전달 시 예외")
        void testUnsupportedPeriod() {
            assertThatThrownBy(() ->
                    service.getTurnover("DAILY", null, null))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.UNSUPPORTED_PERIOD.getMessage());
        }
    }
}

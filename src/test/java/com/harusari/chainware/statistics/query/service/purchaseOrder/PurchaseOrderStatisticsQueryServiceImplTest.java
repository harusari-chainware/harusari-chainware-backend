package com.harusari.chainware.statistics.query.service.purchaseOrder;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.purchaseOrder.PurchaseOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.mapper.PurchaseOrderStatisticsQueryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("[통계 - 본사 발주량] PurchaseOrderStatisticsQueryServiceImpl 테스트")
class PurchaseOrderStatisticsQueryServiceImplTest {

    @Mock
    private PurchaseOrderStatisticsQueryMapper mapper;

    @InjectMocks
    private PurchaseOrderStatisticsQueryServiceImpl service;

    public PurchaseOrderStatisticsQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("1. DAILY + includeProduct = false → 벤더 레벨 호출")
        void testDailyVendorLevel() {
            LocalDate date = LocalDate.of(2025, 6, 20);
            when(mapper.getVendorLevelStatistics(null, date, date)).thenReturn(List.of());

            List<? extends PurchaseOrderStatisticsResponseBase> result =
                    service.getStatistics("DAILY", null, date, false);

            assertThat(result).isEmpty();
            verify(mapper).getVendorLevelStatistics(null, date, date);
        }

        @Test
        @DisplayName("2. WEEKLY + includeProduct = true → 상품 레벨 호출")
        void testWeeklyProductLevel() {
            LocalDate date = LocalDate.of(2025, 6, 17); // 화요일
            when(mapper.getProductLevelStatistics(any(), any(), any())).thenReturn(List.of());

            List<? extends PurchaseOrderStatisticsResponseBase> result =
                    service.getStatistics("WEEKLY", 1L, date.minusWeeks(1), true);

            assertThat(result).isEmpty();
            verify(mapper).getProductLevelStatistics(eq(1L), any(), any());
        }

        @Test
        @DisplayName("3. MONTHLY + includeProduct = false → 벤더 레벨 호출")
        void testMonthlyVendorLevel() {
            LocalDate date = LocalDate.of(2025, 5, 10);
            when(mapper.getVendorLevelStatistics(any(), any(), any())).thenReturn(List.of());

            List<? extends PurchaseOrderStatisticsResponseBase> result =
                    service.getStatistics("MONTHLY", 2L, date, false);

            assertThat(result).isEmpty();
            verify(mapper).getVendorLevelStatistics(eq(2L), any(), any());
        }
    }

    @Nested
    @DisplayName("예외 케이스")
    class ExceptionCases {

        @Test
        @DisplayName("4. WEEKLY 기간이 아직 완료되지 않은 경우 예외")
        void testWeeklyPeriodNotCompleted() {
            LocalDate futureDate = LocalDate.now().with(java.time.DayOfWeek.MONDAY);

            assertThatThrownBy(() ->
                    service.getStatistics("WEEKLY", null, futureDate, false))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.PERIOD_NOT_COMPLETED.getMessage());
        }

        @Test
        @DisplayName("5. MONTHLY 기간이 아직 완료되지 않은 경우 예외")
        void testMonthlyPeriodNotCompleted() {
            LocalDate thisMonth = LocalDate.now().withDayOfMonth(5);

            assertThatThrownBy(() ->
                    service.getStatistics("MONTHLY", null, thisMonth, false))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.PERIOD_NOT_COMPLETED.getMessage());
        }

        @Test
        @DisplayName("6. 지원하지 않는 period → UNSUPPORTED_PERIOD 예외")
        void testUnsupportedPeriod() {
            assertThatThrownBy(() ->
                    service.getStatistics("YEARLY", null, null, false))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.UNSUPPORTED_PERIOD.getMessage());
        }
    }
}

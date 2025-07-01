package com.harusari.chainware.statistics.query.service.storeOrder;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.storeOrder.StoreOrderStatisticsResponseBase;
import com.harusari.chainware.statistics.query.mapper.StoreOrderStatisticsQueryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("[통계 - 가맹점 발주량] StoreOrderStatisticsQueryServiceImpl 테스트")
class StoreOrderStatisticsQueryServiceImplTest {

    @Mock
    private StoreOrderStatisticsQueryMapper mapper;

    @InjectMocks
    private StoreOrderStatisticsQueryServiceImpl service;

    public StoreOrderStatisticsQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("1. DAILY + includeProduct = false → getFranchiseLevelStatistics 호출")
        void testDailyFranchiseLevel() {
            LocalDate date = LocalDate.of(2025, 6, 20);
            when(mapper.getFranchiseLevelStatistics(null, date, date)).thenReturn(List.of());

            List<? extends StoreOrderStatisticsResponseBase> result =
                    service.getStatistics("DAILY", null, date, false);

            assertThat(result).isEmpty();
            verify(mapper).getFranchiseLevelStatistics(null, date, date);
        }

        @Test
        @DisplayName("2. WEEKLY + includeProduct = true → getProductLevelStatistics 호출")
        void testWeeklyProductLevel() {
            LocalDate date = LocalDate.of(2025, 6, 10); // 화요일
            when(mapper.getProductLevelStatistics(any(), any(), any())).thenReturn(List.of());

            List<? extends StoreOrderStatisticsResponseBase> result =
                    service.getStatistics("WEEKLY", 1L, date.minusWeeks(1), true);

            assertThat(result).isEmpty();
            verify(mapper).getProductLevelStatistics(eq(1L), any(), any());
        }

        @Test
        @DisplayName("3. MONTHLY + includeProduct = false → getFranchiseLevelStatistics 호출")
        void testMonthlyFranchiseLevel() {
            LocalDate date = LocalDate.of(2025, 5, 5);
            when(mapper.getFranchiseLevelStatistics(any(), any(), any())).thenReturn(List.of());

            List<? extends StoreOrderStatisticsResponseBase> result =
                    service.getStatistics("MONTHLY", 100L, date, false);

            assertThat(result).isEmpty();
            verify(mapper).getFranchiseLevelStatistics(eq(100L), any(), any());
        }
    }

    @Nested
    @DisplayName("예외 케이스")
    class ExceptionCases {

        @Test
        @DisplayName("4. WEEKLY - 기간이 아직 완료되지 않음")
        void testWeeklyPeriodNotCompleted() {
            // 다음 주 화요일을 사용해야 예외 조건이 확실히 만족됨
            LocalDate futureWeek = LocalDate.now().plusWeeks(1).with(DayOfWeek.TUESDAY);

            assertThatThrownBy(() ->
                    service.getStatistics("WEEKLY", null, futureWeek, false))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.PERIOD_NOT_COMPLETED.getMessage());
        }



        @Test
        @DisplayName("5. MONTHLY - 기간이 아직 완료되지 않음")
        void testMonthlyPeriodNotCompleted() {
            // given
            LocalDate futureMonth = LocalDate.of(2025, 6, 5);
            LocalDate fakeToday = LocalDate.of(2025, 6, 10);

            try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
                mocked.when(LocalDate::now).thenReturn(fakeToday);

                // when & then
                assertThatThrownBy(() ->
                        service.getStatistics("MONTHLY", null, futureMonth, false))
                        .isInstanceOf(StatisticsException.class)
                        .hasMessageContaining(StatisticsErrorCode.PERIOD_NOT_COMPLETED.getMessage());
            }
        }

        @Test
        @DisplayName("6. 지원하지 않는 period 전달 시 예외 발생")
        void testUnsupportedPeriod() {
            assertThatThrownBy(() ->
                    service.getStatistics("QUARTERLY", null, null, false))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.UNSUPPORTED_PERIOD.getMessage());
        }
    }
}

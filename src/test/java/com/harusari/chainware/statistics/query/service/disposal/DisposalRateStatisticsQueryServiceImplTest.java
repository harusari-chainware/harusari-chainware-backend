package com.harusari.chainware.statistics.query.service.disposal;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.disposal.DisposalRateStatisticsResponseBase;
import com.harusari.chainware.statistics.query.mapper.DisposalRateStatisticsQueryMapper;
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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("[통계 - 폐기율] DisposalRateStatisticsQueryServiceImpl 테스트")
class DisposalRateStatisticsQueryServiceImplTest {

    @Mock
    private DisposalRateStatisticsQueryMapper mapper;

    @InjectMocks
    private DisposalRateStatisticsQueryServiceImpl service;

    public DisposalRateStatisticsQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("1. DAILY + includeProduct=false → getDisposalRate 호출")
        void testDailyWithoutProduct() {
            LocalDate date = LocalDate.of(2025, 6, 20);
            when(mapper.getDisposalRate(date, date, null, null)).thenReturn(List.of());

            List<? extends DisposalRateStatisticsResponseBase> result =
                    service.getDisposalStatistics("DAILY", null, null, date, false);

            assertThat(result).isEmpty();
            verify(mapper).getDisposalRate(date, date, null, null);
        }

        @Test
        @DisplayName("2. WEEKLY + includeProduct=false → getDisposalRate 호출")
        void testWeeklyWithoutProduct() {
            LocalDate date = LocalDate.of(2025, 6, 17); // Tuesday
            when(mapper.getDisposalRate(any(), any(), any(), any())).thenReturn(List.of());

            List<? extends DisposalRateStatisticsResponseBase> result =
                    service.getDisposalStatistics("WEEKLY", 1L, null, date.minusWeeks(1), false);

            verify(mapper).getDisposalRate(any(), any(), eq(1L), isNull());
        }

        @Test
        @DisplayName("3. MONTHLY + includeProduct=true → getProductLevelDisposalRate 호출")
        void testMonthlyWithProduct() {
            LocalDate date = LocalDate.of(2024, 5, 15);
            when(mapper.getProductLevelDisposalRate(any(), any(), any(), any())).thenReturn(List.of());

            List<? extends DisposalRateStatisticsResponseBase> result =
                    service.getDisposalStatistics("MONTHLY", null, 2L, date, true);

            verify(mapper).getProductLevelDisposalRate(any(), any(), isNull(), eq(2L));
        }
    }

    @Nested
    @DisplayName("예외 케이스")
    class ExceptionCases {

        @Test
        @DisplayName("4. WEEKLY 기간이 아직 완료되지 않았을 때 예외")
        void testWeeklyPeriodNotCompleted() {
            LocalDate futureWeek = LocalDate.now().plusWeeks(1).with(DayOfWeek.TUESDAY);

            assertThatThrownBy(() ->
                    service.getDisposalStatistics("WEEKLY", null, null, futureWeek, false))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.PERIOD_NOT_COMPLETED.getMessage());
        }

        @Test
        @DisplayName("5. MONTHLY 기간이 아직 완료되지 않았을 때 예외")
        void testMonthlyPeriodNotCompleted() {
            // given
            LocalDate thisMonth = LocalDate.of(2025, 6, 15);
            LocalDate fakeToday = LocalDate.of(2025, 6, 20);

            try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
                mocked.when(LocalDate::now).thenReturn(fakeToday);

                // when & then
                assertThatThrownBy(() ->
                        service.getDisposalStatistics("MONTHLY", null, null, thisMonth, false))
                        .isInstanceOf(StatisticsException.class)
                        .hasMessageContaining(StatisticsErrorCode.PERIOD_NOT_COMPLETED.getMessage());
            }
        }

        @Test
        @DisplayName("6. 지원하지 않는 period 전달 시 예외")
        void testInvalidPeriod() {
            assertThatThrownBy(() ->
                    service.getDisposalStatistics("YEARLY", null, null, null, false))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.UNSUPPORTED_PERIOD.getMessage());
        }
    }
}

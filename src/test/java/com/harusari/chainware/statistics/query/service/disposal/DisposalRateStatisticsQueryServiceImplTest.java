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
            LocalDate expectedStart = date.minusDays(6);
            LocalDate expectedEnd = date;

            when(mapper.getDisposalRate(expectedStart, expectedEnd, null, null)).thenReturn(List.of());

            List<? extends DisposalRateStatisticsResponseBase> result =
                    service.getDisposalStatistics("DAILY", null, null, date, false);

            assertThat(result).isEmpty();
            verify(mapper).getDisposalRate(expectedStart, expectedEnd, null, null);
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

            when(mapper.getProductLevelDisposalRate(any(), any(), any(), any(), any()))
                    .thenReturn(List.of());

            List<? extends DisposalRateStatisticsResponseBase> result =
                    service.getDisposalStatistics("MONTHLY", null, 2L, date, true);

            verify(mapper).getProductLevelDisposalRate(eq("MONTHLY"), any(), any(), isNull(), eq(2L));
        }

        @Nested
        @DisplayName("예외 케이스")
        class ExceptionCases {

            @Test
            @DisplayName("4. WEEKLY 기간이 아직 완료되지 않았을 때 → 더 이상 예외 없이 정상 통계 조회")
            void testWeeklyPeriodAllowPartialData() {
                LocalDate futureDate = LocalDate.of(2025, 7, 15); // baseDate
                LocalDate today = LocalDate.of(2025, 7, 10); // today is before baseDate

                try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
                    mocked.when(LocalDate::now).thenReturn(today);

                    List<? extends DisposalRateStatisticsResponseBase> result =
                            service.getDisposalStatistics("WEEKLY", null, null, futureDate, false);

                    assertThat(result).isNotNull(); // 또는 .isEmpty() 검증도 가능
                }
            }


            @Test
            @DisplayName("5. MONTHLY 현재까지의 데이터로 정상 조회")
            void testMonthlyNowAllowed() {
                LocalDate thisMonth = LocalDate.of(2025, 6, 15);
                LocalDate fakeToday = LocalDate.of(2025, 6, 20);

                try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
                    mocked.when(LocalDate::now).thenReturn(fakeToday);

                    assertThatCode(() ->
                            service.getDisposalStatistics("MONTHLY", null, null, thisMonth, false)
                    ).doesNotThrowAnyException();
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
}

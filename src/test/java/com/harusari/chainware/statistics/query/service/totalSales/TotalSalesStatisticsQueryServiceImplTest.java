package com.harusari.chainware.statistics.query.service.totalSales;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.dto.totalSales.TotalSalesStatisticsResponse;
import com.harusari.chainware.statistics.query.mapper.TotalSalesStatisticsQueryMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("[통계 - 총 매출] TotalSalesStatisticsQueryServiceImpl 테스트")
class TotalSalesStatisticsQueryServiceImplTest {

    @Mock
    private TotalSalesStatisticsQueryMapper mapper;

    @InjectMocks
    private TotalSalesStatisticsQueryServiceImpl service;

    public TotalSalesStatisticsQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("1. DAILY + franchiseId 있음")
        void testDailyWithFranchise() {
            LocalDate date = LocalDate.of(2025, 6, 20);
            long current = 10000L;
            long previous = 8000L;

            when(mapper.getTotalAmount(1L, date, date)).thenReturn(current);
            when(mapper.getTotalAmount(1L, date.minusDays(1), date.minusDays(1))).thenReturn(previous);

            TotalSalesStatisticsResponse response = service.getStatistics("DAILY", 1L, date);

            assertThat(response.getTotalSalesAmount()).isEqualTo(current);
            assertThat(response.getChangeRate()).isEqualTo(25.0);
            assertThat(response.getFranchiseName()).isNull(); // franchiseId != null일 때는 null
        }

        @Test
        @DisplayName("2. WEEKLY + franchiseId 없음")
        void testWeeklyForAll() {
            LocalDate date = LocalDate.of(2025, 6, 12); // 기준일

            LocalDate start = date.minusDays(6);        // 6/6 ~ 6/12
            LocalDate end = date;
            LocalDate prevStart = date.minusDays(13);   // 5/30 ~ 6/5
            LocalDate prevEnd = date.minusDays(7);

            when(mapper.getTotalAmount(null, start, end)).thenReturn(15000L);
            when(mapper.getTotalAmount(null, prevStart, prevEnd)).thenReturn(10000L);

            TotalSalesStatisticsResponse response = service.getStatistics("WEEKLY", null, date);

            assertThat(response.getTotalSalesAmount()).isEqualTo(15000L);
            assertThat(response.getChangeRate()).isEqualTo(50.0);
            assertThat(response.getFranchiseName()).isEqualTo("전체");
        }

        @Test
        @DisplayName("3. MONTHLY + franchiseId 있음")
        void testMonthlyWithFranchise() {
            LocalDate date = LocalDate.of(2025, 6, 5);

            LocalDate start = date.minusDays(29);       // 5/7 ~ 6/5
            LocalDate end = date;
            LocalDate prevStart = date.minusDays(59);   // 4/6 ~ 5/6
            LocalDate prevEnd = date.minusDays(30);

            when(mapper.getTotalAmount(2L, start, end)).thenReturn(20000L);
            when(mapper.getTotalAmount(2L, prevStart, prevEnd)).thenReturn(25000L);

            TotalSalesStatisticsResponse response = service.getStatistics("MONTHLY", 2L, date);

            assertThat(response.getTotalSalesAmount()).isEqualTo(20000L);
            assertThat(response.getChangeRate()).isEqualTo(-20.0);
            assertThat(response.getFranchiseName()).isNull();
        }

        @Nested
        @DisplayName("예외 케이스")
        class ExceptionCases {

            @Test
            @DisplayName("4. 지원하지 않는 period → 예외 발생")
            void testUnsupportedPeriod() {
                assertThatThrownBy(() ->
                        service.getStatistics("YEARLY", null, null))
                        .isInstanceOf(StatisticsException.class)
                        .hasMessageContaining(StatisticsErrorCode.UNSUPPORTED_PERIOD.getMessage());
            }
        }
    }
}

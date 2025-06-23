package com.harusari.chainware.statistics.query.service.salesPattern;

import com.harusari.chainware.statistics.exception.StatisticsErrorCode;
import com.harusari.chainware.statistics.exception.StatisticsException;
import com.harusari.chainware.statistics.query.mapper.SalesPatternMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("[통계 - 판매 패턴] SalesPatternQueryServiceImpl 테스트")
class SalesPatternQueryServiceImplTest {

    @Mock
    private SalesPatternMapper mapper;

    @InjectMocks
    private SalesPatternQueryServiceImpl service;

    public SalesPatternQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("정상 케이스")
    class SuccessCases {

        @Test
        @DisplayName("1. HOURLY + franchiseId 존재 → getHourlySalesByFranchise 호출")
        void testHourlyWithFranchise() {
            LocalDate date = LocalDate.of(2025, 6, 10);
            when(mapper.getHourlySalesByFranchise(anyLong(), any())).thenReturn(List.of());

            service.getSalesPattern("HOURLY", 1L, date);

            verify(mapper).getHourlySalesByFranchise(eq(1L), eq(date));
        }

        @Test
        @DisplayName("2. HOURLY + franchiseId 없음 → getHourlySalesForHeadquarters 호출")
        void testHourlyHeadquarters() {
            LocalDate date = LocalDate.of(2025, 6, 11);
            when(mapper.getHourlySalesForHeadquarters(any())).thenReturn(List.of());

            service.getSalesPattern("HOURLY", null, date);

            verify(mapper).getHourlySalesForHeadquarters(eq(date));
        }

        @Test
        @DisplayName("3. WEEKLY + franchiseId 존재 → getWeekdaySalesByFranchise 호출")
        void testWeeklyWithFranchise() {
            LocalDate date = LocalDate.of(2025, 6, 12);
            when(mapper.getWeekdaySalesByFranchise(anyLong(), any())).thenReturn(List.of());

            service.getSalesPattern("WEEKLY", 2L, date);

            verify(mapper).getWeekdaySalesByFranchise(eq(2L), eq(date));
        }

        @Test
        @DisplayName("4. WEEKLY + franchiseId 없음 → getWeekdaySalesForHeadquarters 호출")
        void testWeeklyHeadquarters() {
            LocalDate date = LocalDate.of(2025, 6, 13);
            when(mapper.getWeekdaySalesForHeadquarters(any())).thenReturn(List.of());

            service.getSalesPattern("WEEKLY", null, date);

            verify(mapper).getWeekdaySalesForHeadquarters(eq(date));
        }

        @Test
        @DisplayName("5. MONTHLY + franchiseId 존재 → getDailySalesByFranchise 호출")
        void testMonthlyWithFranchise() {
            LocalDate date = LocalDate.of(2025, 6, 14);
            when(mapper.getDailySalesByFranchise(anyLong(), any())).thenReturn(List.of());

            service.getSalesPattern("MONTHLY", 3L, date);

            verify(mapper).getDailySalesByFranchise(eq(3L), eq(date));
        }

        @Test
        @DisplayName("6. MONTHLY + franchiseId 없음 → getDailySalesForHeadquarters 호출")
        void testMonthlyHeadquarters() {
            LocalDate date = LocalDate.of(2025, 6, 15);
            when(mapper.getDailySalesForHeadquarters(any())).thenReturn(List.of());

            service.getSalesPattern("MONTHLY", null, date);

            verify(mapper).getDailySalesForHeadquarters(eq(date));
        }
    }

    @Nested
    @DisplayName("예외 케이스")
    class ExceptionCases {

        @Test
        @DisplayName("7. 지원하지 않는 period 전달 시 예외 발생")
        void testUnsupportedPeriod() {
            assertThatThrownBy(() ->
                    service.getSalesPattern("YEARLY", null, LocalDate.now()))
                    .isInstanceOf(StatisticsException.class)
                    .hasMessageContaining(StatisticsErrorCode.UNSUPPORTED_PERIOD.getMessage());
        }
    }
}

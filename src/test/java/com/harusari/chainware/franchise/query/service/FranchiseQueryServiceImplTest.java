package com.harusari.chainware.franchise.query.service;

import com.harusari.chainware.exception.franchise.FranchiseNotFoundException;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.repository.FranchiseQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.harusari.chainware.exception.franchise.FranchiseErrorCode.FRANCHISE_NOT_FOUND_EXCEPTION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[가맹점 - service] FranchiseQueryServiceImpl 테스트")
class FranchiseQueryServiceImplTest {

    @Mock
    private FranchiseQueryRepository franchiseQueryRepository;

    @InjectMocks
    private FranchiseQueryServiceImpl franchiseQueryService;

    @Test
    @DisplayName("[가맹점 조회] 등록된 가맹점 전체를 조회하는 테스트 (페이징)")
    void testSearchFranchises() {
        // given
        FranchiseSearchRequest searchRequest = FranchiseSearchRequest.builder()
                .franchiseName("가맹점")
                .zipcode("12345")
                .addressRoad("서울시")
                .addressDetail("빌딩")
                .franchiseStatus(null)
                .contractStartDate(LocalDate.parse("2025-01-01"))
                .contractEndDate(LocalDate.parse("2025-12-31"))
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());

        FranchiseSearchResponse response1 = FranchiseSearchResponse.builder()
                .franchiseName("가맹점01")
                .franchiseContact("01012341234")
                .build();

        FranchiseSearchResponse response2 = FranchiseSearchResponse.builder()
                .franchiseName("가맹점02")
                .franchiseContact("01056785678")
                .build();

        Page<FranchiseSearchResponse> mockPage = new PageImpl<>(List.of(response1, response2), pageable, 2);

        when(franchiseQueryRepository.pageFranchises(searchRequest, pageable))
                .thenReturn(mockPage);

        // when
        Page<FranchiseSearchResponse> result = franchiseQueryService.searchFranchises(searchRequest, pageable);

        // then
        verify(franchiseQueryRepository, times(1)).pageFranchises(searchRequest, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).franchiseName()).isEqualTo("가맹점01");
        assertThat(result.getContent().get(1).franchiseName()).isEqualTo("가맹점02");

        // 캡처 검증 (옵션)
        ArgumentCaptor<FranchiseSearchRequest> requestCaptor = ArgumentCaptor.forClass(FranchiseSearchRequest.class);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(franchiseQueryRepository).pageFranchises(requestCaptor.capture(), pageableCaptor.capture());
        FranchiseSearchRequest capturedRequest = requestCaptor.getValue();
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedRequest.franchiseName()).isEqualTo("가맹점");
        assertThat(capturedPageable.getPageNumber()).isEqualTo(0);
        assertThat(capturedPageable.getPageSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("[가맹점 상세 조회] ID로 상세 조회 성공 테스트")
    void testGetFranchiseDetailSuccess() {
        // given
        Long franchiseId = 1L;

        FranchiseSearchDetailResponse mockResponse = FranchiseSearchDetailResponse.builder()
                .franchiseId(franchiseId)
                .franchiseName("가맹점01")
                .franchiseContact("01012341234")
                .franchiseTaxId("1234567890")
                .build();

        when(franchiseQueryRepository.findFranchiseDetailById(franchiseId))
                .thenReturn(Optional.of(mockResponse));

        // when
        FranchiseSearchDetailResponse result = franchiseQueryService.getFranchiseDetail(franchiseId);

        // then
        verify(franchiseQueryRepository, times(1)).findFranchiseDetailById(franchiseId);
        assertThat(result).isNotNull();
        assertThat(result.franchiseId()).isEqualTo(franchiseId);
        assertThat(result.franchiseName()).isEqualTo("가맹점01");
        assertThat(result.franchiseContact()).isEqualTo("01012341234");
        assertThat(result.franchiseTaxId()).isEqualTo("1234567890");

        // ArgumentCaptor 검증 (옵션)
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(franchiseQueryRepository).findFranchiseDetailById(captor.capture());
        assertThat(captor.getValue()).isEqualTo(franchiseId);
    }

    @Test
    @DisplayName("[가맹점 상세 조회] ID로 상세 조회 실패 시 예외 발생 테스트")
    void testGetFranchiseDetailNotFound() {
        // given
        Long franchiseId = 999L;

        when(franchiseQueryRepository.findFranchiseDetailById(franchiseId))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> franchiseQueryService.getFranchiseDetail(franchiseId))
                .isInstanceOf(FranchiseNotFoundException.class)
                .hasMessage(FRANCHISE_NOT_FOUND_EXCEPTION.getErrorMessage());

        verify(franchiseQueryRepository, times(1)).findFranchiseDetailById(franchiseId);
    }

}
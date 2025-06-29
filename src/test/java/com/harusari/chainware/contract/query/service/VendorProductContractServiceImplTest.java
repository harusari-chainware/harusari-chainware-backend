package com.harusari.chainware.contract.query.service;

import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListDto;
import com.harusari.chainware.contract.query.mapper.VendorProductContractMapper;
import com.harusari.chainware.exception.contract.ContractAccessDeniedException;
import com.harusari.chainware.exception.contract.ContractErrorCode;
import com.harusari.chainware.exception.contract.ContractNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("[계약 조회 - service] VendorProductContractServiceImpl 테스트")
class VendorProductContractServiceImplTest {

    @InjectMocks
    private VendorProductContractServiceImpl service;

    @Mock
    private VendorProductContractMapper mapper;

    // -- getContracts --

    @Test
    @DisplayName("getContracts: 관리자 조회 시 vendorId 무시, 페이징 결과 반환")
    void getContracts_AsManager() {
        // given
        VendorProductContractSearchRequest req = VendorProductContractSearchRequest.builder()
                .page(1)
                .size(2)
                .vendorId(999L)
                .build();
        boolean isManager = true;
        Long memberId = 100L;

        List<VendorProductContractListDto> list = List.of(
                VendorProductContractListDto.builder()
                        .contractId(1L)
                        .vendorName("VendorA")
                        .productName("ProductX")
                        .basePrice(5000)
                        .contractPrice(4500)
                        .minOrderQty(10)
                        .leadTime(5)
                        .contractStatus(ContractStatus.ACTIVE)
                        .contractStartDate(LocalDate.of(2025, 1, 1))
                        .contractEndDate(LocalDate.of(2025, 12, 31))
                        .build(),
                VendorProductContractListDto.builder()
                        .contractId(2L)
                        .vendorName("VendorB")
                        .productName("ProductY")
                        .basePrice(6000)
                        .contractPrice(5800)
                        .minOrderQty(20)
                        .leadTime(7)
                        .contractStatus(ContractStatus.EXPIRED)
                        .contractStartDate(LocalDate.of(2025, 2, 1))
                        .contractEndDate(LocalDate.of(2025, 11, 30))
                        .build()
        );
        long total = 5L;

        given(mapper.findVendorProductContracts(req, null, true)).willReturn(list);
        given(mapper.countVendorProductContracts(req, null, true)).willReturn(total);

        // when
        PagedResult<VendorProductContractListDto> page = service.getContracts(req, memberId, true);

        // then
        assertThat(page.getContent()).isEqualTo(list);
        assertThat(page.getPagination().getPage()).isEqualTo(1);
        assertThat(page.getPagination().getSize()).isEqualTo(2);
        assertThat(page.getPagination().getTotalElements()).isEqualTo(total);
        assertThat(page.getPagination().getTotalPages()).isEqualTo((int)Math.ceil((double)total/2));
    }

    @Test
    @DisplayName("getContracts: 일반 조회, vendorId 제공 안 하면 예외")
    void getContracts_AsUser_NoVendorId() {
        // given
        VendorProductContractSearchRequest req = VendorProductContractSearchRequest.builder()
                .page(1)
                .size(10)
                .build();
        boolean isManager = false;
        Long memberId = 200L;

        // when / then
        assertThatThrownBy(() -> service.getContracts(req, memberId, false))
            .isInstanceOf(ContractAccessDeniedException.class)
            .hasFieldOrPropertyWithValue("errorCode",
                ContractErrorCode.CONTRACT_ACCESS_DENIED);
    }

    // -- getContractById --

    @Test
    @DisplayName("getContractById: 관리자 성공")
    void getContractById_AsManager() {
        // given
        Long contractId = 100L;
        Long memberId = 300L;
        boolean isManager = true;

        VendorProductContractDto dto = VendorProductContractDto.builder()
                .contractId(100L)
                .vendorId(10L)
                .productId(20L)
                .contractPrice(7000)
                .build();

        given(mapper.findVendorProductContractById(
                eq(contractId), isNull(), eq(isManager)
        )).willReturn(Optional.of(dto));

        // when
        VendorProductContractDto result =
            service.getContractById(contractId, memberId, true);

        // then
        assertThat(result).isEqualTo(dto);
    }

    @Test
    @DisplayName("getContractById: 일반 조회, 매핑된 vendorId 없으면 권한 예외")
    void getContractById_AsUser_NoMapping() {
        // given
        Long contractId = 101L;
        Long memberId = 400L;
        boolean isManager = false;

        given(mapper.findVendorIdByMemberId(memberId)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() ->
            service.getContractById(contractId, memberId, false))
            .isInstanceOf(ContractAccessDeniedException.class)
            .hasFieldOrPropertyWithValue("errorCode",
                ContractErrorCode.CONTRACT_ACCESS_DENIED);
    }

    @Test
    @DisplayName("getContractById: 일반 조회, 계약 없으면 not found 예외")
    void getContractById_AsUser_NotFound() {
        // given
        Long contractId = 102L;
        Long memberId = 500L;
        boolean isManager = false;
        Long vendorId = 50L;

        given(mapper.findVendorIdByMemberId(memberId))
            .willReturn(Optional.of(vendorId));
        given(mapper.findVendorProductContractById(contractId, vendorId, false))
            .willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() ->
            service.getContractById(contractId, memberId, false))
            .isInstanceOf(ContractNotFoundException.class)
            .hasFieldOrPropertyWithValue("errorCode",
                ContractErrorCode.CONTRACT_NOT_FOUND);
    }

    @Test
    @DisplayName("getContractById: 일반 조회 성공")
    void getContractById_AsUser_Success() {
        // given
        Long contractId = 103L;
        Long memberId = 600L;
        boolean isManager = false;
        Long vendorId = 60L;

        VendorProductContractDto dto = VendorProductContractDto.builder()
                .contractId(contractId)
                .vendorId(vendorId)
                .productId(30L)
                .contractPrice(8000)
                .build();

        given(mapper.findVendorIdByMemberId(memberId))
            .willReturn(Optional.of(vendorId));
        given(mapper.findVendorProductContractById(contractId, vendorId, false))
            .willReturn(Optional.of(dto));

        // when
        VendorProductContractDto result =
            service.getContractById(contractId, memberId, false);

        // then
        assertThat(result).isEqualTo(dto);
    }
}

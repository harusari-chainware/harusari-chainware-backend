package com.harusari.chainware.contract.command.application.service;

import com.harusari.chainware.contract.command.application.dto.request.ContractCreateRequest;
import com.harusari.chainware.contract.command.application.dto.request.ContractUpdateRequest;
import com.harusari.chainware.contract.command.application.dto.response.ContractResponse;
import com.harusari.chainware.contract.command.domain.aggregate.Contract;
import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.contract.command.domain.repository.ContractRepository;
import com.harusari.chainware.exception.contract.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@DisplayName("[계약 - service] ContractServiceImpl 테스트")
class ContractServiceImplTest {

    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[계약 생성] 성공 테스트")
    void createContractSuccess() {
        // given
        ContractCreateRequest request = ContractCreateRequest.builder()
                .productId(10L)
                .vendorId(20L)
                .contractPrice(5000)
                .minOrderQty(100)
                .leadTime(7)
                .contractStartDate(LocalDate.of(2025, 7, 1))
                .contractEndDate(LocalDate.of(2025, 12, 31))
                .contractStatus(ContractStatus.ACTIVE)
                .build();

        given(contractRepository.existsByProductIdAndVendorIdAndIsDeletedFalse(10L, 20L))
                .willReturn(false);

        Contract saved = Contract.builder()
                .productId(request.getProductId())
                .vendorId(request.getVendorId())
                .contractPrice(request.getContractPrice())
                .minOrderQty(request.getMinOrderQty())
                .leadTime(request.getLeadTime())
                .contractStartDate(request.getContractStartDate())
                .contractEndDate(request.getContractEndDate())
                .contractStatus(request.getContractStatus())
                .build();
        ReflectionTestUtils.setField(saved, "contractId", 100L);

        given(contractRepository.save(any(Contract.class))).willReturn(saved);

        // when
        ContractResponse response = contractService.createContract(request);

        // then
        assertThat(response.getContractId()).isEqualTo(100L);
        assertThat(response.getProductId()).isEqualTo(10L);
        assertThat(response.getVendorId()).isEqualTo(20L);
        assertThat(response.getContractStatus()).isEqualTo(ContractStatus.ACTIVE);
    }

    @Test
    @DisplayName("[계약 생성] 이미 존재 예외 테스트")
    void createContractAlreadyExists() {
        ContractCreateRequest request = ContractCreateRequest.builder()
                .productId(1L).vendorId(1L)
                .contractStartDate(LocalDate.now())
                .contractEndDate(LocalDate.now().plusDays(1))
                .build();

        given(contractRepository.existsByProductIdAndVendorIdAndIsDeletedFalse(1L, 1L))
                .willReturn(true);

        assertThatThrownBy(() -> contractService.createContract(request))
                .isInstanceOf(ContractAlreadyExistsException.class)
                .hasFieldOrPropertyWithValue("errorCode", ContractErrorCode.CONTRACT_ALREADY_EXISTS);
    }

    @Test
    @DisplayName("[계약 생성] 기간 유효성 예외 테스트")
    void createContractPeriodInvalid() {
        ContractCreateRequest request = ContractCreateRequest.builder()
                .productId(1L).vendorId(1L)
                .contractStartDate(LocalDate.of(2025, 12, 31))
                .contractEndDate(LocalDate.of(2025, 1, 1))
                .build();

        given(contractRepository.existsByProductIdAndVendorIdAndIsDeletedFalse(1L, 1L))
                .willReturn(false);

        assertThatThrownBy(() -> contractService.createContract(request))
                .isInstanceOf(ContractPeriodInvalidException.class)
                .hasFieldOrPropertyWithValue("errorCode", ContractErrorCode.CONTRACT_PERIOD_INVALID);
    }

    @Test
    @DisplayName("[계약 수정] 성공 테스트")
    void updateContractSuccess() {
        // given
        Long contractId = 200L;
        Contract existing = Contract.builder()
                .productId(10L).vendorId(20L)
                .contractPrice(5000).minOrderQty(100).leadTime(7)
                .contractStartDate(LocalDate.of(2025, 1, 1))
                .contractEndDate(LocalDate.of(2025, 6, 30))
                .contractStatus(ContractStatus.ACTIVE)
                .build();
        ReflectionTestUtils.setField(existing, "contractId", contractId);

        ContractUpdateRequest request = ContractUpdateRequest.builder()
                .contractPrice(5500)            // 변경할 필드
                .contractEndDate(LocalDate.of(2025, 12, 31)) // 변경할 필드
                .build();

        given(contractRepository.findById(contractId)).willReturn(Optional.of(existing));

        // when
        ContractResponse response = contractService.updateContract(contractId, request);

        // then
        assertThat(response.getContractId()).isEqualTo(contractId);
        assertThat(response.getContractPrice()).isEqualTo(5500);
        assertThat(response.getContractEndDate())
                .isEqualTo(LocalDate.of(2025, 12, 31));
        // 나머지 필드는 기존 값 유지
        assertThat(response.getMinOrderQty()).isEqualTo(100);
        assertThat(response.getContractStatus()).isEqualTo(ContractStatus.ACTIVE);
    }

    @Test
    @DisplayName("[계약 수정] 존재하지 않음 예외 테스트")
    void updateContractNotFound() {
        given(contractRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                contractService.updateContract(1L, mock(ContractUpdateRequest.class))
        ).isInstanceOf(ContractNotFoundException.class)
         .hasFieldOrPropertyWithValue("errorCode", ContractErrorCode.CONTRACT_NOT_FOUND);
    }

    @Test
    @DisplayName("[계약 수정] 기간 유효성 예외 테스트")
    void updateContractPeriodInvalid() {
        Long contractId = 300L;
        Contract existing = Contract.builder()
                .contractStartDate(LocalDate.of(2025, 1, 1))
                .contractEndDate(LocalDate.of(2025, 6, 30))
                .build();
        ReflectionTestUtils.setField(existing, "contractId", contractId);
        given(contractRepository.findById(contractId)).willReturn(Optional.of(existing));

        ContractUpdateRequest request = ContractUpdateRequest.builder()
                .contractStartDate(LocalDate.of(2025, 12, 31))
                .contractEndDate(LocalDate.of(2025, 1, 1))
                .build();

        assertThatThrownBy(() ->
                contractService.updateContract(contractId, request)
        ).isInstanceOf(ContractPeriodInvalidException.class)
         .hasFieldOrPropertyWithValue("errorCode", ContractErrorCode.CONTRACT_PERIOD_INVALID);
    }

    @Test
    @DisplayName("[계약 삭제] 성공 테스트")
    void deleteContractSuccess() {
        Long contractId = 400L;
        Contract spyContract = Mockito.spy(Contract.builder().build());
        ReflectionTestUtils.setField(spyContract, "contractId", contractId);

        given(contractRepository.findById(contractId)).willReturn(Optional.of(spyContract));

        // when
        contractService.deleteContract(contractId);

        // then
        then(spyContract).should().markAsDeleted();
    }

    @Test
    @DisplayName("[계약 삭제] 존재하지 않음 예외 테스트")
    void deleteContractNotFound() {
        given(contractRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> contractService.deleteContract(1L))
                .isInstanceOf(ContractNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", ContractErrorCode.CONTRACT_NOT_FOUND);
    }

    @Test
    @DisplayName("[계약 조회] 단건 성공 테스트")
    void getContractByIdSuccess() {
        Long contractId = 500L;
        Contract existing = Contract.builder()
                .productId(11L).vendorId(22L)
                .contractPrice(6000).minOrderQty(150).leadTime(5)
                .contractStartDate(LocalDate.of(2025, 2, 1))
                .contractEndDate(LocalDate.of(2025, 8, 31))
                .contractStatus(ContractStatus.EXPIRED)
                .build();
        ReflectionTestUtils.setField(existing, "contractId", contractId);

        given(contractRepository.findById(contractId)).willReturn(Optional.of(existing));

        ContractResponse response = contractService.getContractById(contractId);

        assertThat(response.getContractId()).isEqualTo(contractId);
        assertThat(response.getVendorId()).isEqualTo(22L);
    }

    @Test
    @DisplayName("[계약 조회] 단건 존재하지 않음 예외 테스트")
    void getContractByIdNotFound() {
        given(contractRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(() -> contractService.getContractById(1L))
                .isInstanceOf(ContractNotFoundException.class)
                .hasFieldOrPropertyWithValue("errorCode", ContractErrorCode.CONTRACT_NOT_FOUND);
    }

    @Test
    @DisplayName("[계약 조회] 공급사별 리스트 성공 테스트")
    void getContractsByVendorSuccess() {
        Contract c1 = Contract.builder().build();
        Contract c2 = Contract.builder().build();
        given(contractRepository.findByVendorIdAndIsDeletedFalse(10L))
                .willReturn(List.of(c1, c2));

        List<ContractResponse> responses = contractService.getContractsByVendor(10L);
        assertThat(responses).hasSize(2);
    }

    @Test
    @DisplayName("[계약 조회] 상품별 리스트 성공 테스트")
    void getContractsByProductSuccess() {
        Contract c1 = Contract.builder().build();
        given(contractRepository.findByProductIdAndIsDeletedFalse(20L))
                .willReturn(List.of(c1));

        List<ContractResponse> responses = contractService.getContractsByProduct(20L);
        assertThat(responses).hasSize(1);
    }
}

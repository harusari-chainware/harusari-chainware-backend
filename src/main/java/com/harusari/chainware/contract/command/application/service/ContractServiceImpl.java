package com.harusari.chainware.contract.command.application.service;

import com.harusari.chainware.contract.command.application.dto.request.ContractCreateRequest;
import com.harusari.chainware.contract.command.application.dto.request.ContractUpdateRequest;
import com.harusari.chainware.contract.command.application.dto.response.ContractResponse;
import com.harusari.chainware.contract.command.domain.aggregate.Contract;
import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.contract.command.domain.repository.ContractRepository;
import com.harusari.chainware.exception.contract.ContractAlreadyExistsException;
import com.harusari.chainware.exception.contract.ContractErrorCode;
import com.harusari.chainware.exception.contract.ContractNotFoundException;
import com.harusari.chainware.exception.contract.ContractPeriodInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Override
    @Transactional
    public ContractResponse createContract(ContractCreateRequest request) {

        if (contractRepository.existsByProductIdAndVendorIdAndIsDeletedFalse(
                request.getProductId(), request.getVendorId())) {
            throw new ContractAlreadyExistsException(ContractErrorCode.CONTRACT_ALREADY_EXISTS);
        }

        if (request.getContractStartDate().isAfter(request.getContractEndDate())) {
            throw new ContractPeriodInvalidException(ContractErrorCode.CONTRACT_PERIOD_INVALID);
        }

        Contract contract = Contract.builder()
                .productId(request.getProductId())
                .vendorId(request.getVendorId())
                .contractPrice(request.getContractPrice())
                .minOrderQty(request.getMinOrderQty())
                .leadTime(request.getLeadTime())
                .contractStartDate(request.getContractStartDate())
                .contractEndDate(request.getContractEndDate())
                .contractStatus(request.getContractStatus())
                .build();

        Contract saved = contractRepository.save(contract);
        return toResponse(saved);
    }

    @Override
    @Transactional
    public ContractResponse updateContract(Long contractId, ContractUpdateRequest  request) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(ContractErrorCode.CONTRACT_NOT_FOUND));

        // null 값 아닌 필드만 업데이트
        Long productId = request.getProductId() != null ? request.getProductId() : contract.getProductId();
        Long vendorId = request.getVendorId() != null ? request.getVendorId() : contract.getVendorId();
        Integer contractPrice = request.getContractPrice() != null ? request.getContractPrice() : contract.getContractPrice();
        Integer minOrderQty = request.getMinOrderQty() != null ? request.getMinOrderQty() : contract.getMinOrderQty();
        Integer leadTime = request.getLeadTime() != null ? request.getLeadTime() : contract.getLeadTime();
        LocalDate contractStartDate = request.getContractStartDate() != null ? request.getContractStartDate() : contract.getContractStartDate();
        LocalDate contractEndDate = request.getContractEndDate() != null ? request.getContractEndDate() : contract.getContractEndDate();
        ContractStatus contractStatus = request.getContractStatus() != null ? request.getContractStatus() : contract.getContractStatus();

        // 유효성 체크
        if (contractStartDate != null && contractEndDate != null
                && contractStartDate.isAfter(contractEndDate)) {
            throw new ContractPeriodInvalidException(ContractErrorCode.CONTRACT_PERIOD_INVALID);
        }

        // 업데이트 반영 메서드 호출
        contract.updateContractFields(
                productId,
                vendorId,
                contractPrice,
                minOrderQty,
                leadTime,
                contractStartDate,
                contractEndDate,
                contractStatus
        );

        return ContractResponse.builder()
                .contractId(contract.getContractId())
                .productId(contract.getProductId())
                .vendorId(contract.getVendorId())
                .contractPrice(contract.getContractPrice())
                .minOrderQty(contract.getMinOrderQty())
                .leadTime(contract.getLeadTime())
                .contractStartDate(contract.getContractStartDate())
                .contractEndDate(contract.getContractEndDate())
                .contractStatus(contract.getContractStatus())
                .build();
    }


    @Override
    @Transactional
    public void deleteContract(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(ContractErrorCode.CONTRACT_NOT_FOUND));
        // 예외 예: 삭제 불가 조건 체크 필요 시 예외 던짐
        contract.markAsDeleted();
    }

    @Override
    @Transactional(readOnly = true)
    public ContractResponse getContractById(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(ContractErrorCode.CONTRACT_NOT_FOUND));
        return toResponse(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponse> getContractsByVendor(Long vendorId) {
        return contractRepository.findByVendorIdAndIsDeletedFalse(vendorId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractResponse> getContractsByProduct(Long productId) {
        return contractRepository.findByProductIdAndIsDeletedFalse(productId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private ContractResponse toResponse(Contract contract) {
        return ContractResponse.builder()
                .contractId(contract.getContractId())
                .productId(contract.getProductId())
                .vendorId(contract.getVendorId())
                .contractPrice(contract.getContractPrice())
                .minOrderQty(contract.getMinOrderQty())
                .leadTime(contract.getLeadTime())
                .contractStartDate(contract.getContractStartDate())
                .contractEndDate(contract.getContractEndDate())
                .contractStatus(contract.getContractStatus())
                .build();
    }
}

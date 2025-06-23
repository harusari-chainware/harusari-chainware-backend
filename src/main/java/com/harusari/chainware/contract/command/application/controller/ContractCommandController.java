package com.harusari.chainware.contract.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.contract.command.application.dto.request.ContractCreateRequest;
import com.harusari.chainware.contract.command.application.dto.request.ContractUpdateRequest;
import com.harusari.chainware.contract.command.application.dto.response.ContractResponse;
import com.harusari.chainware.contract.command.application.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contract")
@RequiredArgsConstructor
public class ContractCommandController {

    private final ContractService contractService;

    // 계약 생성
    @PostMapping
    public ResponseEntity<ApiResponse<ContractResponse>> createContract(
            @RequestBody @Valid ContractCreateRequest request) {
        ContractResponse response = contractService.createContract(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 계약 수정
    @PutMapping("/{contractId}")
    public ResponseEntity<ApiResponse<ContractResponse>>  updateContract(
            @PathVariable Long contractId,
            @RequestBody @Valid ContractUpdateRequest request) {
        ContractResponse response = contractService.updateContract(contractId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 계약 삭제 (soft delete)
    @PutMapping("/{contractId}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteContract(@PathVariable Long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
package com.harusari.chainware.contract.command.application.controller;

import com.harusari.chainware.contract.command.application.dto.request.ContractCreateRequest;
import com.harusari.chainware.contract.command.application.dto.request.ContractUpdateRequest;
import com.harusari.chainware.contract.command.application.dto.response.ContractResponse;
import com.harusari.chainware.contract.command.application.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // 계약 생성
    @PostMapping("/contract/headquarters")
    public ResponseEntity<ContractResponse> createContract(
            @RequestBody @Valid ContractCreateRequest request) {
        ContractResponse response = contractService.createContract(request);
        return ResponseEntity.ok(response);
    }

    // 계약 수정
    @PutMapping("/contract/headquarters/{contractId}")
    public ResponseEntity<ContractResponse> updateContract(
            @PathVariable Long contractId,
            @RequestBody @Valid ContractUpdateRequest request) {
        ContractResponse response = contractService.updateContract(contractId, request);
        return ResponseEntity.ok(response);
    }

    // 계약 삭제 (soft delete)
    @PutMapping("/contract/headquarters/{contractId}/delete")
    public ResponseEntity<Void> deleteContract(@PathVariable Long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.noContent().build();
    }
}
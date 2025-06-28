package com.harusari.chainware.contract.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.contract.command.application.dto.request.ContractCreateRequest;
import com.harusari.chainware.contract.command.application.dto.request.ContractUpdateRequest;
import com.harusari.chainware.contract.command.application.dto.response.ContractResponse;
import com.harusari.chainware.contract.command.application.service.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contract")
@RequiredArgsConstructor
@Tag(name = "계약 API", description = "계약 생성, 수정, 삭제 API")
public class ContractCommandController {

    private final ContractService contractService;

    @Operation(summary = "계약 생성", description = "새로운 계약을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "계약 생성 성공")
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ContractResponse>> createContract(
            @RequestBody(description = "계약 생성 요청 정보")
            @Valid @org.springframework.web.bind.annotation.RequestBody ContractCreateRequest request
    ) {
        ContractResponse response = contractService.createContract(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "계약 수정", description = "기존 계약의 정보를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "계약 수정 성공")
    })
    @PutMapping("/{contractId}")
    public ResponseEntity<ApiResponse<ContractResponse>> updateContract(
            @Parameter(name = "contractId", description = "수정할 계약의 ID", example = "42")
            @PathVariable Long contractId,
            @RequestBody(description = "계약 수정 요청 정보")
            @Valid @org.springframework.web.bind.annotation.RequestBody ContractUpdateRequest request
    ) {
        ContractResponse response = contractService.updateContract(contractId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "계약 삭제", description = "지정한 계약을 논리 삭제 처리합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "계약 삭제 성공")
    })
    @DeleteMapping("/{contractId}")
    public ResponseEntity<ApiResponse<Void>> deleteContract(
            @Parameter(name = "contractId", description = "삭제할 계약의 ID", example = "42")
            @PathVariable Long contractId
    ) {
        contractService.deleteContract(contractId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
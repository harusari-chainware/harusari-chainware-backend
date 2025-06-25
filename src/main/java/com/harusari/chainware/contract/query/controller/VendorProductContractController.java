package com.harusari.chainware.contract.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.service.VendorProductContractService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contracts")
public class VendorProductContractController {

    private final VendorProductContractService contractService;

    // 1. 전체 목록
    @GetMapping
    public ResponseEntity<ApiResponse<List<VendorProductContractDto>>> getAllContracts() {
        List<VendorProductContractDto> result = contractService.getAllContracts();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    // 2. 특정 거래처 ID 기준 목록
    @GetMapping("/{vendorId}")
    public ResponseEntity<ApiResponse<List<VendorProductContractDto>>> getContractsByVendor(@PathVariable Long vendorId) {
        List<VendorProductContractDto> result = contractService.getContractsByVendorId(vendorId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
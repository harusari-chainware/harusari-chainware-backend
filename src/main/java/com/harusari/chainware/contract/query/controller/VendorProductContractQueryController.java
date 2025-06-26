package com.harusari.chainware.contract.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListResponse;
import com.harusari.chainware.contract.query.service.VendorProductContractService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contracts")
public class VendorProductContractQueryController{

    private final VendorProductContractService contractService;

    // 1. 전체 목록
//    @GetMapping
//    public ResponseEntity<ApiResponse<List<VendorProductContractDto>>> getAllContracts() {
//        List<VendorProductContractDto> result = contractService.getAllContracts();
//        return ResponseEntity.ok(ApiResponse.success(result));
//    }
//
//    @GetMapping
//    public ResponseEntity<ApiResponse<VendorProductContractListResponse>> getAllContracts(
//            @AuthenticationPrincipal CustomUserDetails userDetails,
//            @RequestParam(required = false) Integer page,
//            @RequestParam(required = false) Integer size) {
//
//        MemberAuthorityType authority = userDetails.getMemberAuthorityType();
//
//        boolean isManager = List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER)
//                .contains(authority);
//        boolean isVendorManager = authority.equals(VENDOR_MANAGER);
//
//        // vendorId는 관리자면 null, 벤더매니저면 본인 거래처로 고정
////        Long vendorId = isVendorManager ? userDetails.getVendorId() : null;
//
//        VendorProductContractSearchRequest request = VendorProductContractSearchRequest.builder()
//                .vendorId(vendorId)
//                .page(page != null ? page : 1)
//                .size(size != null ? size : 10)
//                .build();
//
//        VendorProductContractListResponse result = contractService.getAllContracts(request, isManager);
//        return ResponseEntity.ok(ApiResponse.success(result));
//    }

    // 2. 특정 거래처 ID 기준 목록
    @GetMapping("/{vendorId}")
    public ResponseEntity<ApiResponse<List<VendorProductContractDto>>> getContractsByVendor(@PathVariable Long vendorId) {
        List<VendorProductContractDto> result = contractService.getContractsByVendorId(vendorId);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
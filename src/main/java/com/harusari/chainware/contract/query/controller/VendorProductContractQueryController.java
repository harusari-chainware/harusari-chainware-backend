package com.harusari.chainware.contract.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.service.VendorProductContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contracts")
public class VendorProductContractQueryController{

    private final VendorProductContractService contractService;

        @GetMapping
        public ResponseEntity<ApiResponse<PagedResult<VendorProductContractDto>>> getContracts(
                @AuthenticationPrincipal CustomUserDetails userDetails,
                @ModelAttribute VendorProductContractSearchRequest request) {

            boolean isManager = switch (userDetails.getMemberAuthorityType()) {
                case GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER -> true;
                default -> false;
            };

            Long memberId = userDetails.getMemberId();
            PagedResult<VendorProductContractDto> result = contractService.getContracts(request, memberId, isManager);
            return ResponseEntity.ok(ApiResponse.success(result));
        }
    }

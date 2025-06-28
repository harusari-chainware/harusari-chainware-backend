package com.harusari.chainware.contract.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListDto;
import com.harusari.chainware.contract.query.service.VendorProductContractService;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contracts")
public class VendorProductContractQueryController{

    private final VendorProductContractService contractService;


    @GetMapping
    public ResponseEntity<ApiResponse<PagedResult<VendorProductContractListDto>>> getContracts(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String topCategoryName,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String vendorName,
            @RequestParam(required = false) VendorType vendorType,
            @RequestParam(required = false) ContractStatus contractStatus,
            @RequestParam(required = false) String contractStartDate,
            @RequestParam(required = false) String contractEndDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        boolean isManager = switch (userDetails.getMemberAuthorityType()) {
            case GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER -> true;
            default -> false;
        };
        Long memberId = userDetails.getMemberId();

        LocalDate startDate = contractStartDate != null ? LocalDate.parse(contractStartDate) : null;
        LocalDate endDate = contractEndDate != null ? LocalDate.parse(contractEndDate) : null;

        VendorProductContractSearchRequest request = VendorProductContractSearchRequest.builder()
                .productName(productName)
                .topCategoryName(topCategoryName)
                .categoryName(categoryName)
                .vendorName(vendorName)
                .vendorType(vendorType)
                .contractStatus(contractStatus)
                .contractStartDate(startDate)
                .contractEndDate(endDate)
                .vendorId(isManager ? null : memberId)
                .page(page)
                .size(size)
                .build();

        PagedResult<VendorProductContractListDto> result = contractService.getContracts(
                request, memberId, isManager
        );
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ApiResponse<VendorProductContractDto>> getContract(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long contractId
    ) {
        boolean isManager = switch (userDetails.getMemberAuthorityType()) {
            case GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER -> true;
            default -> false;
        };
        Long memberId = userDetails.getMemberId();

        VendorProductContractDto dto = contractService.getContractById(
                contractId, memberId, isManager
        );
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}

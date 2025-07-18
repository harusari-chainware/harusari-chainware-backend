package com.harusari.chainware.contract.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PagedResult;
import com.harusari.chainware.contract.command.domain.aggregate.ContractStatus;
import com.harusari.chainware.contract.query.dto.request.VendorProductContractSearchRequest;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractDto;
import com.harusari.chainware.contract.query.dto.response.VendorProductContractListDto;
import com.harusari.chainware.contract.query.service.VendorProductContractService;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorStatus;
import com.harusari.chainware.vendor.command.domain.aggregate.VendorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contracts")
@Tag(name = "거래처별 계약 제품 조회 API", description = "거래처별 계약 제품 목록 조회 및 단일 계약 제품 조회 기능")
public class VendorProductContractQueryController {

    private final VendorProductContractService contractService;

    @Operation(summary = "거래처별 계약 제품 목록 조회", description = "사용자 권한에 따라 거래처별 계약 제품 목록을 조회하고, 옵션별 필터 및 페이징을 적용합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "계약 제품 목록 조회 성공")
    })
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResult<VendorProductContractListDto>>> getContracts(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "제품 이름", example = "아메리카노") @RequestParam(required = false) String productName,
            @Parameter(description = "상위 카테고리 이름", example = "식품") @RequestParam(required = false) String topCategoryName,
            @Parameter(description = "하위 카테고리 이름", example = "완제품") @RequestParam(required = false) String categoryName,
            @Parameter(description = "거래처 이름", example = "ABC유통") @RequestParam(required = false) String vendorName,
            @Parameter(description = "거래처 유형") @RequestParam(required = false) VendorType vendorType,
            @Parameter(description = "거래처 상태") @RequestParam(required = false) VendorStatus vendorStatus,
            @Parameter(description = "계약 상태") @RequestParam(required = false) ContractStatus contractStatus,
            @Parameter(description = "계약 기준일(YYYY-MM-DD)", example = "2025-01-01") @RequestParam(required = false) String contractDate,
            @Parameter(description = "계약 시작일(YYYY-MM-DD)", example = "2025-01-01") @RequestParam(required = false) String contractStartDate,
            @Parameter(description = "계약 종료일(YYYY-MM-DD)", example = "2025-12-31") @RequestParam(required = false) String contractEndDate,
            @Parameter(description = "페이지 번호", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam(defaultValue = "10") int size
    ) {
        boolean isManager = switch (userDetails.getMemberAuthorityType()) {
            case GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, SUPER_ADMIN -> true;
            default -> false;
        };
        Long memberId = userDetails.getMemberId();

        LocalDate date = contractDate != null ? LocalDate.parse(contractDate) : null;

        LocalDate startDate = contractStartDate != null ? LocalDate.parse(contractStartDate) : null;
        LocalDate endDate = contractEndDate != null ? LocalDate.parse(contractEndDate) : null;

        VendorProductContractSearchRequest request = VendorProductContractSearchRequest.builder()
                .productName(productName)
                .topCategoryName(topCategoryName)
                .categoryName(categoryName)
                .vendorName(vendorName)
                .vendorType(vendorType)
                .vendorStatus(vendorStatus)
                .contractStatus(contractStatus)
                .contractDate(date)
                .contractStartDate(startDate)
                .contractEndDate(endDate)
                .page(page)
                .size(size)
                .build();

        PagedResult<VendorProductContractListDto> result =
                contractService.getContracts(request, memberId, isManager);

        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(summary = "단일 계약 제품 조회", description = "계약 ID를 통해 단일 계약 제품 정보를 조회하며, 사용자 권한에 따라 접근을 제어합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "단일 계약 제품 조회 성공")
    })
    @GetMapping("/{contractId}")
    public ResponseEntity<ApiResponse<VendorProductContractDto>> getContract(
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "조회할 계약의 ID", example = "123") @PathVariable Long contractId
    ) {
        boolean isManager = switch (userDetails.getMemberAuthorityType()) {
            case GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, SUPER_ADMIN -> true;
            default -> false;
        };
        Long memberId = userDetails.getMemberId();

        VendorProductContractDto dto = contractService.getContractById(
                contractId, memberId, isManager
        );
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
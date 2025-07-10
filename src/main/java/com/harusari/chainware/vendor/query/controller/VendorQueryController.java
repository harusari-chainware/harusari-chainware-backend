package com.harusari.chainware.vendor.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorContractInfoResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorPresignedUrlResponse;
import com.harusari.chainware.vendor.query.dto.response.VendorDetailResponse;
import com.harusari.chainware.vendor.query.dto.request.VendorSearchRequest;
import com.harusari.chainware.vendor.query.dto.response.VendorSearchResponse;
import com.harusari.chainware.vendor.query.service.VendorQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "거래처 Query API", description = "거래처 Query API (조회)")
public class VendorQueryController {

    private final VendorQueryService vendorQueryService;

    @Operation(summary = "거래처 목록 조회", description = "검색 조건과 페이징 조건에 맞춰 거래처 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "거래처 목록 조회 성공")
    })
    @GetMapping("/vendors")
    public ResponseEntity<ApiResponse<PageResponse<VendorSearchResponse>>> searchVendors(
            @Parameter(description = "거래처 검색 조건")
            @ModelAttribute VendorSearchRequest vendorSearchRequest,

            @Parameter(description = "페이지네이션 정보")
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<VendorSearchResponse> vendorSearchResponse = vendorQueryService.searchVendors(vendorSearchRequest, pageable);
        PageResponse<VendorSearchResponse> pageResponse = PageResponse.from(vendorSearchResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "거래처 상세 조회", description = "vendorId에 해당하는 거래처의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "거래처 상세 조회 성공"),
    })
    @GetMapping("/vendors/{vendorId}")
    public ResponseEntity<ApiResponse<VendorDetailResponse>> getVendorDetail(
            @Parameter(description = "거래처 ID")
            @PathVariable(name = "vendorId") Long vendorId
    ) {
        VendorDetailResponse vendorDetailResponse = vendorQueryService.getVendorDetail(vendorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(vendorDetailResponse));
    }

    @Operation(summary = "거래처 계약서 Presigned URL 조회", description = "vendorId에 해당하는 거래처의 계약서 다운로드 Presigned URL을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Presigned URL 생성 성공"),
    })
    @GetMapping("/vendors/{vendorId}/agreement/download")
    public ResponseEntity<ApiResponse<VendorPresignedUrlResponse>> getAgreementDownloadUrl(
            @Parameter(description = "거래처 ID")
            @PathVariable(name = "vendorId") Long vendorId
    ) {
        VendorPresignedUrlResponse presignedUrlResponse= vendorQueryService.generateDownloadUrl(vendorId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(presignedUrlResponse));
    }

    @GetMapping("/vendors/{vendorName}/contract-info")
    public ResponseEntity<ApiResponse<VendorContractInfoResponse>> getVendorContractInfo(
            @PathVariable(name = "vendorName") String vendorName
    ) {
        VendorContractInfoResponse vendorContractInfoResponse = vendorQueryService.getVendorContractInfo(vendorName);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(vendorContractInfoResponse));
    }

}
package com.harusari.chainware.franchise.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchisePresignedUrlResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.service.FranchiseQueryService;
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
public class FranchiseQueryController {

    private final FranchiseQueryService franchiseQueryService;

    @GetMapping("/franchises")
    public ResponseEntity<ApiResponse<PageResponse<FranchiseSearchResponse>>> searchFranchises(
            @ModelAttribute FranchiseSearchRequest franchiseSearchRequest,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<FranchiseSearchResponse> franchises = franchiseQueryService.searchFranchises(franchiseSearchRequest, pageable);
        PageResponse<FranchiseSearchResponse> pageResponse = PageResponse.from(franchises);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

    @GetMapping("/franchises/{franchiseId}")
    public ResponseEntity<ApiResponse<FranchiseSearchDetailResponse>> searchFranchise(
            @PathVariable(name = "franchiseId") Long franchiseId
    ) {
        FranchiseSearchDetailResponse franchiseSearchDetailResponse = franchiseQueryService.getFranchiseDetail(franchiseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(franchiseSearchDetailResponse));
    }

    @GetMapping("/franchises/{franchiseId}/agreement/download")
    public ResponseEntity<ApiResponse<FranchisePresignedUrlResponse>> getAgreementDownloadUrl(
            @PathVariable(name = "franchiseId") Long franchiseId
    ) {
        FranchisePresignedUrlResponse presignedUrlResponse = franchiseQueryService.generateDownloadUrl(franchiseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(presignedUrlResponse));
    }

}
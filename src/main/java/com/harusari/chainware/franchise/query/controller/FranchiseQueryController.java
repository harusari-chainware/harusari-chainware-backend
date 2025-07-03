package com.harusari.chainware.franchise.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.franchise.query.dto.request.FranchiseSearchRequest;
import com.harusari.chainware.franchise.query.dto.resposne.FranchisePresignedUrlResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchDetailResponse;
import com.harusari.chainware.franchise.query.dto.resposne.FranchiseSearchResponse;
import com.harusari.chainware.franchise.query.service.FranchiseQueryService;
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
@Tag(name = "가맹점 Query API", description = "가맹점 Query API (조회)")
public class FranchiseQueryController {

    private final FranchiseQueryService franchiseQueryService;

    @Operation(summary = "가맹점 목록 조회", description = "검색 조건과 페이징 조건에 맞춰 가맹점 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가맹점 목록 조회 성공")
    })
    @GetMapping("/franchises")
    public ResponseEntity<ApiResponse<PageResponse<FranchiseSearchResponse>>> searchFranchises(
            @Parameter(description = "가맹점 검색 조건")
            @ModelAttribute FranchiseSearchRequest franchiseSearchRequest,

            @Parameter(description = "페이지네이션 정보")
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<FranchiseSearchResponse> franchises = franchiseQueryService.searchFranchises(franchiseSearchRequest, pageable);
        PageResponse<FranchiseSearchResponse> pageResponse = PageResponse.from(franchises);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "가맹점 상세 조회", description = "franchiseId에 해당하는 가맹점의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가맹점 상세 조회 성공"),
    })
    @GetMapping("/franchises/{franchiseId}")
    public ResponseEntity<ApiResponse<FranchiseSearchDetailResponse>> searchFranchise(
            @Parameter(description = "가맹점 ID")
            @PathVariable(name = "franchiseId") Long franchiseId
    ) {
        FranchiseSearchDetailResponse franchiseSearchDetailResponse = franchiseQueryService.getFranchiseDetail(franchiseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(franchiseSearchDetailResponse));
    }

    @Operation(summary = "가맹점 계약서 Presigned URL 조회", description = "franchiseId에 해당하는 가맹점의 계약서 다운로드 Presigned URL을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Presigned URL 생성 성공"),
    })
    @GetMapping("/franchises/{franchiseId}/agreement/download")
    public ResponseEntity<ApiResponse<FranchisePresignedUrlResponse>> getAgreementDownloadUrl(
            @Parameter(description = "가맹점 ID")
            @PathVariable(name = "franchiseId") Long franchiseId
    ) {
        FranchisePresignedUrlResponse presignedUrlResponse = franchiseQueryService.generateDownloadUrl(franchiseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(presignedUrlResponse));
    }

}
package com.harusari.chainware.disposal.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.disposal.query.dto.*;
import com.harusari.chainware.disposal.query.service.DisposalQueryService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disposal")
@RequiredArgsConstructor
@Tag(name = "폐기 조회 API", description = "폐기 이력을 조회하는 API입니다. 권한에 따라 조회 범위가 제한됩니다.")
public class DisposalQueryController {

    private final DisposalQueryService disposalQueryService;

    @Operation(
            summary = "폐기 내역 조회",
            description = """
            폐기 내역을 검색 조건에 따라 조회합니다.  
            - 가맹점 관리자: 본인 가맹점 폐기만 조회 가능  
            - 창고 관리자: 본인 창고 폐기만 조회 가능  
            - 일반 관리자 이상: 전체 폐기 내역 조회 가능  
            - 검색 조건: 가맹점명, 창고명, 상품명, 카테고리명, 폐기 유형 등  
            - 페이징 지원 (page, size)
        """,
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "폐기 내역 조회 성공")
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponse<DisposalListResponseDto>> getDisposals(
            @Parameter(description = "검색 조건 및 페이징 정보") @ModelAttribute DisposalSearchRequestDto request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MemberAuthorityType authorityType = userDetails.getMemberAuthorityType();
        DisposalListResponseDto result = disposalQueryService.getDisposals(request, memberId, authorityType);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @Operation(
            summary = "일반 폐기 상품 검색",
            description = "상품명 또는 상품코드로 폐기 등록 가능한 상품을 검색합니다. 검색 유형(type)은 WAREHOUSE 또는 FRANCHISE 중 하나입니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상품 검색 성공")
            }
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DisposalProductSearchResponseDto>>> searchProducts(
            @Parameter(description = "상품 검색 조건") @ModelAttribute DisposalProductSearchRequestDto request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MemberAuthorityType authorityType = userDetails.getMemberAuthorityType();
        return ResponseEntity.ok(ApiResponse.success(disposalQueryService.searchProducts(request, memberId, authorityType)));
    }

    @Operation(
            summary = "반품 폐기 상품 목록 조회",
            description = "반품 ID를 기준으로 반품에 포함된 상품 목록을 조회합니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 상품 조회 성공")
            }
    )
    @GetMapping("/products/takeback/{takeBackId}")
    public ResponseEntity<ApiResponse<List<DisposalProductSearchResponseDto>>> getTakeBackProducts(
            @Parameter(description = "반품 ID", example = "3") @PathVariable Long takeBackId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(disposalQueryService.getTakeBackProducts(takeBackId))
        );
    }

    @Operation(
            summary = "반품 코드 검색",
            description = "반품 코드(keyword)를 기준으로 반품 내역을 검색합니다. 권한에 따라 검색 결과가 제한됩니다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "반품 코드 검색 성공")
            }
    )
    @GetMapping("/takebacks/search")
    public ResponseEntity<ApiResponse<List<TakeBackSimpleResponseDto>>> searchTakeBacks(
            @Parameter(description = "검색할 반품 코드 키워드", example = "RET") @RequestParam String keyword,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MemberAuthorityType authority = userDetails.getMemberAuthorityType();
        return ResponseEntity.ok(
                ApiResponse.success(disposalQueryService.searchTakeBacks(keyword, memberId, authority))
        );
    }
}



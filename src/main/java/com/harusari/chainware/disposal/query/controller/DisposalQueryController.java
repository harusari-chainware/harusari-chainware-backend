package com.harusari.chainware.disposal.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.disposal.query.dto.DisposalListResponseDto;
import com.harusari.chainware.disposal.query.dto.DisposalSearchRequestDto;
import com.harusari.chainware.disposal.query.service.DisposalQueryService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
            """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "폐기 내역 조회 성공")
    @GetMapping
    public ResponseEntity<ApiResponse<DisposalListResponseDto>> getDisposals(
            @Parameter(description = "검색 조건 및 페이징 정보") @ModelAttribute DisposalSearchRequestDto request,

            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MemberAuthorityType authorityType = userDetails.getMemberAuthorityType();

        DisposalListResponseDto result = disposalQueryService.getDisposals(request, memberId, authorityType);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

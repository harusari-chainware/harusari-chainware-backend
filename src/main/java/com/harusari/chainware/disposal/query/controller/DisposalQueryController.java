package com.harusari.chainware.disposal.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.disposal.query.dto.DisposalListResponseDto;
import com.harusari.chainware.disposal.query.dto.DisposalSearchRequestDto;
import com.harusari.chainware.disposal.query.service.DisposalQueryService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/disposal")
@RequiredArgsConstructor
public class DisposalQueryController {

    private final DisposalQueryService disposalQueryService;
/*
    @GetMapping
    public ResponseEntity<ApiResponse<DisposalListResponseDto>> getDisposals(
            @ModelAttribute DisposalSearchRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MemberAuthorityType authorityType = userDetails.getAuthorityType();

        DisposalListResponseDto result = disposalQueryService.getDisposals(request, memberId, authorityType);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
*/
    @GetMapping
    public ResponseEntity<ApiResponse<DisposalListResponseDto>> getDisposals(
            @ModelAttribute DisposalSearchRequestDto request
    ) {
        Long memberId = 1L;
        MemberAuthorityType authorityType = MemberAuthorityType.GENERAL_MANAGER;

        DisposalListResponseDto result = disposalQueryService.getDisposals(request, memberId, authorityType);
        return ResponseEntity.ok(ApiResponse.success(result));
    }
}

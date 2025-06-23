package com.harusari.chainware.disposal.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.disposal.command.application.dto.DisposalCommandRequestDto;
import com.harusari.chainware.disposal.command.application.service.DisposalCommandService;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/disposal")
@RequiredArgsConstructor
public class DisposalCommandController {

    private final DisposalCommandService disposalCommandService;


    @PostMapping
    public ResponseEntity<ApiResponse<Void>> registerDisposal(
            @RequestBody DisposalCommandRequestDto request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MemberAuthorityType authorityType = userDetails.getMemberAuthorityType();

        disposalCommandService.registerDisposal(request, memberId, authorityType);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
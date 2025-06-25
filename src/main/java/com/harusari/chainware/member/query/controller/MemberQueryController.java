package com.harusari.chainware.member.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.query.dto.request.MemberSearchRequest;
import com.harusari.chainware.member.query.dto.response.LoginHistoryResponse;
import com.harusari.chainware.member.query.dto.response.MemberSearchDetailResponse;
import com.harusari.chainware.member.query.dto.response.MemberSearchResponse;
import com.harusari.chainware.member.query.service.MemberQueryService;
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
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    @GetMapping("/members/email-exists")
    public ResponseEntity<ApiResponse<EmailExistsResponse>> checkEmailDuplicate(
            @RequestParam(name = "email") String email
    ) {
        EmailExistsResponse emailExistsResponse = memberQueryService.checkEmailDuplicate(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(emailExistsResponse));
    }

    @GetMapping("/members")
    public ResponseEntity<ApiResponse<PageResponse<MemberSearchResponse>>> searchMembers(
            @ModelAttribute MemberSearchRequest memberSearchRequest,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<MemberSearchResponse> members = memberQueryService.searchMembers(memberSearchRequest, pageable);
        PageResponse<MemberSearchResponse> pageResponse = PageResponse.from(members);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

    @GetMapping("/members/{memberId}")
    public ResponseEntity<ApiResponse<MemberSearchDetailResponse>> getMemberDetail(
            @PathVariable(name = "memberId") Long memberId
    ) {
        MemberSearchDetailResponse memberSearchDetailResponse = memberQueryService.getMemberDetail(memberId);

        return  ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(memberSearchDetailResponse));
    }

    @GetMapping("/members/{memberId}/login-history")
    public ResponseEntity<ApiResponse<PageResponse<LoginHistoryResponse>>> searchLoginHistory(
        @PathVariable(name = "memberId") Long memberId,
        @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<LoginHistoryResponse> loginHistoryResponse = memberQueryService.searchMemberLoginHistory(memberId, pageable);
        PageResponse<LoginHistoryResponse> pageResponse = PageResponse.from(loginHistoryResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

}
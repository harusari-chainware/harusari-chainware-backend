package com.harusari.chainware.member.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.query.dto.request.MemberSearchRequest;
import com.harusari.chainware.member.query.dto.response.LoginHistoryResponse;
import com.harusari.chainware.member.query.dto.response.MemberSearchDetailResponse;
import com.harusari.chainware.member.query.dto.response.MemberSearchResponse;
import com.harusari.chainware.member.query.dto.response.MyMemberDetailResponse;
import com.harusari.chainware.member.query.service.MemberQueryService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "회원 Query API", description = "회원 정보 조회, 로그인 내역 조회 API")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;

    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 여부를 확인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 중복 여부 확인 성공")
    })
    @GetMapping("/members/email-exists")
    public ResponseEntity<ApiResponse<EmailExistsResponse>> checkEmailDuplicate(
            @Parameter(description = "중복 확인할 이메일", required = true)
            @RequestParam(name = "email") String email
    ) {
        EmailExistsResponse emailExistsResponse = memberQueryService.checkEmailDuplicate(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(emailExistsResponse));
    }

    @Operation(summary = "회원 목록 조회", description = "검색 조건에 따른 회원 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 목록 조회 성공")
    })
    @GetMapping("/members")
    public ResponseEntity<ApiResponse<PageResponse<MemberSearchResponse>>> searchMembers(
            @Parameter(description = "회원 검색 조건") @ModelAttribute MemberSearchRequest memberSearchRequest,
            @Parameter(description = "페이징 정보") @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<MemberSearchResponse> members = memberQueryService.searchMembers(memberSearchRequest, pageable);
        PageResponse<MemberSearchResponse> pageResponse = PageResponse.from(members);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

    @Operation(summary = "회원 상세 조회 [마스터]", description = "특정 회원의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 상세 조회 성공")
    })
    @GetMapping("/members/{memberId}")
    public ResponseEntity<ApiResponse<MemberSearchDetailResponse>> getMemberDetail(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable(name = "memberId") Long memberId
    ) {
        MemberSearchDetailResponse memberSearchDetailResponse = memberQueryService.getMemberDetail(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(memberSearchDetailResponse));
    }

    @Operation(summary = "회원 상세 조회 [회원 본인]", description = "특정 회원의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 상세 조회 성공")
    })
    @GetMapping("/members/me")
    public ResponseEntity<ApiResponse<MyMemberDetailResponse>> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        MyMemberDetailResponse myMemberDetailResponse = memberQueryService.getMyProfile(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(myMemberDetailResponse));
    }

    @Operation(summary = "회원 로그인 이력 조회", description = "회원의 로그인 이력을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 이력 조회 성공")
    })
    @GetMapping("/members/{memberId}/login-history")
    public ResponseEntity<ApiResponse<PageResponse<LoginHistoryResponse>>> searchLoginHistory(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable(name = "memberId") Long memberId,
            @Parameter(description = "페이징 정보") @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<LoginHistoryResponse> loginHistoryResponse = memberQueryService.searchMemberLoginHistory(memberId, pageable);
        PageResponse<LoginHistoryResponse> pageResponse = PageResponse.from(loginHistoryResponse);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(pageResponse));
    }

}
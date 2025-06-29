package com.harusari.chainware.member.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import com.harusari.chainware.member.command.application.dto.request.PasswordChangeRequest;
import com.harusari.chainware.member.command.application.dto.request.UpdateMemberRequest;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.member.command.application.dto.request.warehouse.MemberWithWarehouseRequest;
import com.harusari.chainware.member.command.application.service.MemberCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "회원 Command API", description = "회원 등록, 수정, 비밀번호 변경 API")
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    @Operation(summary = "본사 회원 등록", description = "본사 회원을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "본사 회원 등록 성공")
    })
    @PostMapping("/members/headquarters")
    public ResponseEntity<ApiResponse<Void>> registerHeadquartersMember(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "본사 회원 등록 요청")
            @RequestBody MemberCreateRequest memberCreateRequest
    ) {
        memberCommandService.registerHeadquartersMember(memberCreateRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "가맹점 회원 등록, 가맹점 정보 등록", description = "가맹점 회원을 등록합니다. (form-data)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "가맹점 회원 등록, 가맹점 정보 등록 성공")
    })
    @PostMapping("/members/franchise")
    public ResponseEntity<ApiResponse<Void>> registerFranchiseMember(
            @RequestPart MemberWithFranchiseRequest memberWithFranchiseRequest,
            @Parameter(description = "가맹점 계약서 파일 (PDF)")
            @RequestPart MultipartFile agreementFile
    ) {
        memberCommandService.registerFranchise(memberWithFranchiseRequest, agreementFile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "거래처 회원 등록, 거래처 정보 등록", description = "거래처 회원을 등록합니다. (form-data)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "거래처 회원 등록, 거래처 정보 등록 성공")
    })
    @PostMapping("/members/vendor")
    public ResponseEntity<ApiResponse<Void>> registerVendorMember(
            @RequestPart MemberWithVendorRequest memberWithVendorRequest,
            @Parameter(description = "거래처 계약서 파일 (PDF)")
            @RequestPart MultipartFile agreementFile
    ) {
        memberCommandService.registerVendor(memberWithVendorRequest, agreementFile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "창고 회원 등록, 창고 정보 등록", description = "창고 회원을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "창고 회원 등록, 창고 정보 등록 성공")
    })
    @PostMapping("/members/warehouse")
    public ResponseEntity<ApiResponse<Void>> registerWarehouseMember(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "창고 회원 등록 요청")
            @RequestBody MemberWithWarehouseRequest memberWithWarehouseRequest
    ) {
        memberCommandService.registerWarehouse(memberWithWarehouseRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "비밀번호 변경", description = "회원 비밀번호를 변경합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "비밀번호 변경 성공")
    })
    @PutMapping("/members/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "비밀번호 변경 요청")
            @RequestBody PasswordChangeRequest passwordChangeRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        String email = customUserDetails.getEmail();
        memberCommandService.changePassword(passwordChangeRequest, email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 정보 수정 성공")
    })
    @PutMapping("/members/{memberId}")
    public ResponseEntity<ApiResponse<Void>> updateMember(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable(name = "memberId") Long memberId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "회원 수정 요청")
            @RequestBody UpdateMemberRequest updateMemberRequest
    ) {
        memberCommandService.updateMemberRequest(memberId, updateMemberRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 처리합니다. (Soft Delete)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원 탈퇴 성공")
    })
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(
            @Parameter(description = "회원 ID", required = true)
            @PathVariable Long memberId
    ) {
        memberCommandService.deleteMemberRequest(memberId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

}
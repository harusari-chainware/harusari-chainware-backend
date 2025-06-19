package com.harusari.chainware.member.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import com.harusari.chainware.member.command.application.dto.request.franchise.MemberWithFranchiseRequest;
import com.harusari.chainware.member.command.application.dto.request.vendor.MemberWithVendorRequest;
import com.harusari.chainware.member.command.application.dto.request.warehouse.MemberWithWarehouseRequest;
import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.command.application.service.MemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MemberCommandController {

    private final MemberCommandService memberCommandService;

    @GetMapping("/members/email-exists")
    public ResponseEntity<ApiResponse<EmailExistsResponse>> checkEmailDuplicate(
            @RequestParam(name = "email") String email
    ) {
        EmailExistsResponse emailExistsResponse = memberCommandService.checkEmailDuplicate(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(emailExistsResponse));
    }

    @PostMapping("/members/headquarters")
    public ResponseEntity<ApiResponse<Void>> registerHeadquartersMember(
        @RequestBody MemberCreateRequest memberCreateRequest
    ) {
        memberCommandService.registerHeadquartersMember(memberCreateRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PostMapping("/members/franchise")
    public ResponseEntity<ApiResponse<Void>> registerFranchiseMember(
            @RequestPart MemberWithFranchiseRequest memberWithFranchiseRequest,
            @RequestPart MultipartFile agreementFile
    ) {
        memberCommandService.registerFranchise(memberWithFranchiseRequest, agreementFile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PostMapping("/members/vendor")
    public ResponseEntity<ApiResponse<Void>> registerVendorMember(
            @RequestPart MemberWithVendorRequest memberWithVendorRequest,
            @RequestPart MultipartFile agreementFile
    ) {
        memberCommandService.registerVendor(memberWithVendorRequest, agreementFile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

    @PostMapping("/members/warehouse")
    public ResponseEntity<ApiResponse<Void>> registerWarehouseMember(
            @RequestBody MemberWithWarehouseRequest memberWithWarehouseRequest
    ) {
        memberCommandService.registerWarehouse(memberWithWarehouseRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(null));
    }

}
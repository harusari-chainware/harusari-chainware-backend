package com.harusari.chainware.member.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.query.service.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
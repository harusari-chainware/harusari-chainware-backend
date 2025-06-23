package com.harusari.chainware.auth.controller;

import com.harusari.chainware.auth.dto.request.LoginRequest;
import com.harusari.chainware.auth.dto.request.RefreshTokenRequest;
import com.harusari.chainware.auth.dto.response.TokenResponse;
import com.harusari.chainware.auth.service.AuthService;
import com.harusari.chainware.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @RequestBody LoginRequest loginRequest
    ) {
        TokenResponse token = authService.login(loginRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<TokenResponse>> logout(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        authService.logout(refreshTokenRequest.refreshToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        TokenResponse tokenResponse = authService.refreshToken(refreshTokenRequest.refreshToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(tokenResponse));
    }

}
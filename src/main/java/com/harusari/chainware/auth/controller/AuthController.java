package com.harusari.chainware.auth.controller;

import com.harusari.chainware.auth.dto.request.LoginRequest;
import com.harusari.chainware.auth.dto.request.RefreshTokenRequest;
import com.harusari.chainware.auth.dto.response.TokenResponse;
import com.harusari.chainware.auth.service.AuthService;
import com.harusari.chainware.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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
@Tag(name = "인증 API", description = "로그인, 로그아웃, 토큰 재발급 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인", description = "아이디와 비밀번호를 이용해 로그인을 수행하고 JWT 토큰을 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "로그인 요청", required = true)
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest
    ) {
        TokenResponse token = authService.login(loginRequest, httpServletRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(token));
    }

    @Operation(summary = "로그아웃", description = "리프레시 토큰을 사용하여 로그아웃을 수행합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "리프레시 토큰 요청", required = true)
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        authService.logout(refreshTokenRequest.refreshToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(null));
    }

    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용해 새로운 JWT 토큰을 발급합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공")
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "리프레시 토큰 요청", required = true)
            @RequestBody RefreshTokenRequest refreshTokenRequest
    ) {
        TokenResponse tokenResponse = authService.refreshToken(refreshTokenRequest.refreshToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(tokenResponse));
    }

}
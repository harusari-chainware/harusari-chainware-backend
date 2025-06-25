package com.harusari.chainware.auth.service;

import com.harusari.chainware.auth.dto.request.LoginRequest;
import com.harusari.chainware.auth.dto.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {

    TokenResponse login(LoginRequest loginRequest, HttpServletRequest httpServletRequest);

    TokenResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

}
package com.harusari.chainware.auth.service;

import com.harusari.chainware.auth.dto.request.LoginRequest;
import com.harusari.chainware.auth.dto.response.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest loginRequest);

    TokenResponse refreshToken(String refreshToken);

    void logout(String refreshToken);

}
package com.harusari.chainware.auth.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record RefreshTokenDTO(
        String email, String refreshToken, Date expiryDate
) {
}
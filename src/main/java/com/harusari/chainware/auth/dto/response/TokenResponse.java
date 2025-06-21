package com.harusari.chainware.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(
    String accessToken, String refreshToken
) {
}
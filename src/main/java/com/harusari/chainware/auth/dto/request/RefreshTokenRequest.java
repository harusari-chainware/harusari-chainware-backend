package com.harusari.chainware.auth.dto.request;

import lombok.Builder;

@Builder
public record RefreshTokenRequest(
        String refreshToken
) {
}
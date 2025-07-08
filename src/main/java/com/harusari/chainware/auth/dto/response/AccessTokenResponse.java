package com.harusari.chainware.auth.dto.response;

import lombok.Builder;

@Builder
public record AccessTokenResponse(
        String accessToken
) {
}
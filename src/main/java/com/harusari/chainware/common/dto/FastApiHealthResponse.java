package com.harusari.chainware.common.dto;

import lombok.Builder;

@Builder
public record FastApiHealthResponse(
        String status
) {
}
package com.harusari.chainware.member.query.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoginHistoryResponse(
        Long memberId, LocalDateTime loginAt, String ipAddress, String browser
) {
}
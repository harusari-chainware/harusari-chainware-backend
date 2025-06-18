package com.harusari.chainware.member.command.application.dto.response;

import lombok.Builder;

@Builder
public record EmailExistsResponse(
        boolean exists,
        String validationToken
) {
}
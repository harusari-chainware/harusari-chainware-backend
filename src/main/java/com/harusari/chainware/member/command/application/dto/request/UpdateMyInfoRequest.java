package com.harusari.chainware.member.command.application.dto.request;

import lombok.Builder;

@Builder
public record UpdateMyInfoRequest(
        String phoneNumber
) {
}
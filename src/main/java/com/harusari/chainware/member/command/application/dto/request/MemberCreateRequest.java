package com.harusari.chainware.member.command.application.dto.request;

import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MemberCreateRequest(
        String email, String password, String name, String phoneNumber,
        LocalDate birthDate, String position, MemberAuthorityType authorityName, String validationToken
) {
}
package com.harusari.chainware.member.query.dto.request;

import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record MemberSearchRequest(
        String email, MemberAuthorityType authorityName, String position,
        LocalDate joinDateFrom, LocalDate joinDateTo, boolean isDeleted
) {
}
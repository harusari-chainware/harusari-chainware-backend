package com.harusari.chainware.member.command.application.dto.request;

import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.Builder;

@Builder
public record UpdateMemberRequest(
        String name, MemberAuthorityType authorityName,
        String phoneNumber, String position
) {
}
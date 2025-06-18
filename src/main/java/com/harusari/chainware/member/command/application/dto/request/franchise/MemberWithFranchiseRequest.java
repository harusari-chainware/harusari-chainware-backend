package com.harusari.chainware.member.command.application.dto.request.franchise;

import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import lombok.Builder;

@Builder
public record MemberWithFranchiseRequest(
        MemberCreateRequest memberCreateRequest,
        FranchiseCreateRequest franchiseCreateRequest
) {
}
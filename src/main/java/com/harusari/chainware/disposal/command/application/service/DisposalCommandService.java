package com.harusari.chainware.disposal.command.application.service;

import com.harusari.chainware.disposal.command.application.dto.DisposalCommandRequestDto;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;

public interface DisposalCommandService {

    void registerDisposal(DisposalCommandRequestDto request, Long memberId, MemberAuthorityType authorityType);
}

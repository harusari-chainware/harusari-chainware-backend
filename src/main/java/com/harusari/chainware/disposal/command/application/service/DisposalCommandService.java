package com.harusari.chainware.disposal.command.application.service;

import com.harusari.chainware.disposal.command.application.dto.DisposalCommandRequestDto;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import org.springframework.transaction.annotation.Transactional;

public interface DisposalCommandService {

    @Transactional
    void registerDisposal(DisposalCommandRequestDto request, Long memberId, MemberAuthorityType authorityType);
}

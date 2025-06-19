package com.harusari.chainware.disposal.query.service;

import com.harusari.chainware.disposal.query.dto.DisposalListResponseDto;
import com.harusari.chainware.disposal.query.dto.DisposalSearchRequestDto;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;

public interface DisposalQueryService {

    DisposalListResponseDto getDisposals(DisposalSearchRequestDto request, Long memberId, MemberAuthorityType authorityType);

}

package com.harusari.chainware.disposal.query.service;

import com.harusari.chainware.disposal.query.dto.*;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;

import java.util.List;

public interface DisposalQueryService {

    DisposalListResponseDto getDisposals(DisposalSearchRequestDto request, Long memberId, MemberAuthorityType authorityType);

    List<DisposalProductSearchResponseDto> searchProducts(DisposalProductSearchRequestDto request, Long memberId, MemberAuthorityType authorityType);

    List<DisposalProductSearchResponseDto> getTakeBackProducts(Long takeBackId);

    List<TakeBackSimpleResponseDto> searchTakeBacks(String keyword, Long memberId, MemberAuthorityType authority);

}

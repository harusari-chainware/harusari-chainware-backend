package com.harusari.chainware.franchise.common.mapstruct;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.member.command.application.dto.request.franchise.FranchiseCreateRequest;
import org.mapstruct.Mapper;

@Mapper
public interface FranchiseMapStruct {

    Franchise toFranchise(FranchiseCreateRequest franchiseCreateRequest, Long memberId);

}
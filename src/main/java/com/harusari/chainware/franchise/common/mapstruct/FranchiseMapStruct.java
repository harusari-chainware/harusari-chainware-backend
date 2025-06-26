package com.harusari.chainware.franchise.common.mapstruct;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.member.command.application.dto.request.franchise.FranchiseCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedSourcePolicy =  ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface FranchiseMapStruct {

    Franchise toFranchise(FranchiseCreateRequest franchiseCreateRequest, Long memberId);

}
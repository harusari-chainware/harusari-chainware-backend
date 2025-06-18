package com.harusari.chainware.franchise.common.mapper;

import com.harusari.chainware.franchise.command.domain.aggregate.Franchise;
import com.harusari.chainware.member.command.application.dto.request.franchise.FranchiseCreateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedSourcePolicy =  ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface FranchiseMapStruct {

    Franchise toFranchise(FranchiseCreateRequest franchiseCreateRequest, Long memberId);

}
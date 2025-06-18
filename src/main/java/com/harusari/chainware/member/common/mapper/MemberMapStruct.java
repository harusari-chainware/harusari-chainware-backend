package com.harusari.chainware.member.common.mapper;

import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import com.harusari.chainware.member.command.domain.aggregate.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedSourcePolicy =  ReportingPolicy.WARN,
        unmappedTargetPolicy = ReportingPolicy.WARN
)
public interface MemberMapStruct {

    Member toMember(MemberCreateRequest memberCreateRequest);

}
package com.harusari.chainware.member.common.mapstruct;

import com.harusari.chainware.member.command.application.dto.request.MemberCreateRequest;
import com.harusari.chainware.member.command.domain.aggregate.Member;
import org.mapstruct.Mapper;

@Mapper
public interface MemberMapStruct {

    Member toMember(MemberCreateRequest memberCreateRequest);

}
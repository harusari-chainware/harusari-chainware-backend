package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;

public interface MemberCommandRepositoryCustom {

    Member findActiveMemberByEmail(String email);

}
package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;

public interface MemberCommandRepository {

    boolean existsByEmail(String email);

    Member save(Member member);

}
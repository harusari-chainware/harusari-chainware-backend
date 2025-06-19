package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;

public interface MemberRepository {

    boolean existsByEmail(String email);

    Member save(Member member);

}
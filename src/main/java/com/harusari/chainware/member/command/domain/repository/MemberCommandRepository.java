package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;

import java.util.Optional;

public interface MemberCommandRepository {

    boolean existsByEmail(String email);

    Member save(Member member);

    Optional<Member> findMemberByMemberId(Long memberId);

}
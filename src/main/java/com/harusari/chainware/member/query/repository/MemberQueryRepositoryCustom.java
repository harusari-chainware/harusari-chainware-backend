package com.harusari.chainware.member.query.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;

import java.util.Optional;

public interface MemberQueryRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<Member> findActiveMemberByEmail(String email);

}
package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberCommandRepository extends MemberCommandRepositoryCustom, JpaRepository<Member, Long> {

    boolean existsByEmail(String email);

    Optional<Member> findMemberByMemberId(Long memberId);

}
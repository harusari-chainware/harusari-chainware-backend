package com.harusari.chainware.member.query.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberQueryRepository extends MemberQueryRepositoryCustom, JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

}
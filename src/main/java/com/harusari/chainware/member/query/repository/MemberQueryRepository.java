package com.harusari.chainware.member.query.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQueryRepository extends MemberQueryRepositoryCustom, JpaRepository<Member, Long> {

}
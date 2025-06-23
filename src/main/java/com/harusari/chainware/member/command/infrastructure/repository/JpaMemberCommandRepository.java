package com.harusari.chainware.member.command.infrastructure.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.repository.MemberCommandRepository;
import com.harusari.chainware.member.command.domain.repository.MemberCommandRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberCommandRepository extends MemberCommandRepository, MemberCommandRepositoryCustom, JpaRepository<Member, Long> {

}
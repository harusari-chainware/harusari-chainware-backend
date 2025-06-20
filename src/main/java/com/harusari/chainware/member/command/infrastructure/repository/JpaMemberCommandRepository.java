package com.harusari.chainware.member.command.infrastructure.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.repository.MemberCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMemberCommandRepository extends MemberCommandRepository, JpaRepository<Member, Long> {


}
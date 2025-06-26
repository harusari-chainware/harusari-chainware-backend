package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Authority;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityCommandRepository extends JpaRepository<Authority, Long> {

    Authority findByAuthorityName(MemberAuthorityType authorityName);

    Authority findByAuthorityId(Integer authorityId);

}
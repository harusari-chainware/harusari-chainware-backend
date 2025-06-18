package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.Authority;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;

public interface AuthorityRepository {

    Authority findByAuthorityName(MemberAuthorityType authorityName);

}
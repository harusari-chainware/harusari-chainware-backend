package com.harusari.chainware.member.command.infrastructure.repository;

import com.harusari.chainware.member.command.domain.aggregate.Authority;
import com.harusari.chainware.member.command.domain.repository.AuthorityCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthorityCommandRepository extends AuthorityCommandRepository, JpaRepository<Authority, Integer> {

}
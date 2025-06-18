package com.harusari.chainware.member.command.infrastructure.repository;

import com.harusari.chainware.member.command.domain.aggregate.Authority;
import com.harusari.chainware.member.command.domain.repository.AuthorityRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuthorityRepository extends AuthorityRepository, JpaRepository<Authority, Integer> {

}
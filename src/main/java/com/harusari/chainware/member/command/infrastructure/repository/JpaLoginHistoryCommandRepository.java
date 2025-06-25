package com.harusari.chainware.member.command.infrastructure.repository;

import com.harusari.chainware.member.command.domain.aggregate.LoginHistory;
import com.harusari.chainware.member.command.domain.repository.LoginHistoryCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaLoginHistoryCommandRepository extends LoginHistoryCommandRepository, JpaRepository<LoginHistory, Long> {

}
package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginHistoryCommandRepository extends JpaRepository<LoginHistory, Long> {

}
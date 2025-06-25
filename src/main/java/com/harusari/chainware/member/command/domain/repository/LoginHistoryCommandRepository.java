package com.harusari.chainware.member.command.domain.repository;

import com.harusari.chainware.member.command.domain.aggregate.LoginHistory;

public interface LoginHistoryCommandRepository {

    LoginHistory save(LoginHistory loginHistory);

}
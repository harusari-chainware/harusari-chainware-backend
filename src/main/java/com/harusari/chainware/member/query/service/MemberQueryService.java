package com.harusari.chainware.member.query.service;

import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;

public interface MemberQueryService {

    EmailExistsResponse checkEmailDuplicate(String email);

}
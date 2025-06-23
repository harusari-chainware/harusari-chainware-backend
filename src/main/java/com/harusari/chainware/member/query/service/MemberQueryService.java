package com.harusari.chainware.member.query.service;

import com.harusari.chainware.member.command.application.dto.response.EmailExistsResponse;
import com.harusari.chainware.member.query.dto.request.MemberSearchRequest;
import com.harusari.chainware.member.query.dto.response.MemberSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryService {

    EmailExistsResponse checkEmailDuplicate(String email);

    Page<MemberSearchResponse> searchMembers(MemberSearchRequest memberSearchRequest, Pageable pageable);

}
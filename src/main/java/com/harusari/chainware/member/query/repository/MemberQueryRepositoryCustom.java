package com.harusari.chainware.member.query.repository;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.query.dto.request.MemberSearchRequest;
import com.harusari.chainware.member.query.dto.response.MemberSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberQueryRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<Member> findActiveMemberByEmail(String email);

    Page<MemberSearchResponse> findMembers(MemberSearchRequest condition, Pageable pageable);

}
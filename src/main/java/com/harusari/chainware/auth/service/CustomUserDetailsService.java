package com.harusari.chainware.auth.service;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.exception.auth.MemberNotFoundException;
import com.harusari.chainware.member.command.domain.aggregate.Authority;
import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.repository.AuthorityCommandRepository;
import com.harusari.chainware.member.query.repository.MemberQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.harusari.chainware.exception.auth.AuthErrorCode.MEMBER_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberQueryRepository memberQueryRepository;
    private final AuthorityCommandRepository authorityCommandRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberQueryRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND_EXCEPTION));

        Authority authority = authorityCommandRepository.findByAuthorityId(member.getAuthorityId());

        return new CustomUserDetails(member.getMemberId(), member.getEmail(), authority.getAuthorityName());
    }

}
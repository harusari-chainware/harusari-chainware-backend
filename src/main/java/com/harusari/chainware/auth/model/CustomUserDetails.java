package com.harusari.chainware.auth.model;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final String email;
    private final MemberAuthorityType memberAuthorityType;

    public CustomUserDetails(Member member) {
        this.memberId = member.getMemberId();
        this.email = member.getEmail();
        this.memberAuthorityType = MemberAuthorityType.of(member.getAuthorityId());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(memberAuthorityType::name);
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
package com.harusari.chainware.securitysupport;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.member.command.domain.aggregate.Member;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member member = createMemberByPosition(annotation.position(), annotation.memberId());
        CustomUserDetails principal = new CustomUserDetails(
                member.getMemberId(),
                member.getEmail(),
                MemberAuthorityType.of(member.getAuthorityId())
        );


        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }

    private Member createMemberByPosition(String position, Long memberId) {
        return switch (position.toUpperCase()) {
            case "MASTER" -> MemberTestFactory.testMaster(memberId);
            case "GENERAL_MANAGER" -> MemberTestFactory.testGeneralManager(memberId);
            case "SENIOR_MANAGER" -> MemberTestFactory.testSeniorManager(memberId);
            case "WAREHOUSE_MANAGER" -> MemberTestFactory.testWarehouseManager(memberId);
            case "FRANCHISE_MANAGER" -> MemberTestFactory.testFranchiseManager(memberId);
            case "VENDOR_MANAGER" -> MemberTestFactory.testVendorManager(memberId);
            case "SYSTEM" -> MemberTestFactory.testSystem(memberId);
            default -> throw new IllegalArgumentException("Invalid position: " + position);
        };
    }
}


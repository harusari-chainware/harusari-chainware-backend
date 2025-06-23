package com.harusari.chainware.securitysupport;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberTestFactory {

    public static Member testMaster(Long id) {
        return createMember(id, 1, "master@domain.com", "MASTER");
    }

    public static Member testGeneralManager(Long id) {
        return createMember(id, 2, "general@domain.com", "GENERAL_MANAGER");
    }

    public static Member testSeniorManager(Long id) {
        return createMember(id, 3, "senior@domain.com", "SENIOR_MANAGER");
    }

    public static Member testWarehouseManager(Long id) {
        return createMember(id, 4, "warehouse@domain.com", "WAREHOUSE_MANAGER");
    }

    public static Member testFranchiseManager(Long id) {
        return createMember(id, 5, "franchise@domain.com", "FRANCHISE_MANAGER");
    }

    public static Member testVendorManager(Long id) {
        return createMember(id, 6, "vendor@domain.com", "VENDOR_MANAGER");
    }

    public static Member testSystem(Long id) {
        return createMember(id, 99, "system@domain.com", "SYSTEM");
    }

    private static Member createMember(Long id, int authorityId, String email, String position) {
        Member member = Member.builder()
                .authorityId(authorityId)
                .email(email)
                .password("encodedPassword")
                .name("테스트 유저")
                .phoneNumber("010-1111-2222")
                .birthDate(LocalDate.of(1990, 1, 1))
                .position(position)
                .modifiedAt(LocalDateTime.now())
                .isDeleted(false)
                .build();

        ReflectionTestUtils.setField(member, "memberId", id);
        return member;
    }
}


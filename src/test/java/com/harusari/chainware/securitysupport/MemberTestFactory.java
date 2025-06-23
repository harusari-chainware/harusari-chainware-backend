package com.harusari.chainware.securitysupport;

import com.harusari.chainware.member.command.domain.aggregate.Member;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MemberTestFactory {

    public static Member testMaster(Long id) {
        return createMember(id, "master@domain.com", "MASTER");
    }

    public static Member testGeneralManager(Long id) {
        return createMember(id, "general@domain.com", "GENERAL_MANAGER");
    }

    public static Member testSeniorManager(Long id) {
        return createMember(id, "senior@domain.com", "SENIOR_MANAGER");
    }

    public static Member testWarehouseManager(Long id) {
        return createMember(id, "warehouse@domain.com", "WAREHOUSE_MANAGER");
    }

    public static Member testFranchiseManager(Long id) {
        return createMember(id, "franchise@domain.com", "FRANCHISE_MANAGER");
    }

    public static Member testVendorManager(Long id) {
        return createMember(id, "vendor@domain.com", "VENDOR_MANAGER");
    }

    public static Member testSystem(Long id) {
        return createMember(id, "system@domain.com", "SYSTEM");
    }

    private static Member createMember(Long id, String email, String position) {
        Member member = Member.builder()
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


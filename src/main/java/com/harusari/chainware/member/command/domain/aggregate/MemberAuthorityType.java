package com.harusari.chainware.member.command.domain.aggregate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberAuthorityType {

    MASTER("마스터"),
    GENERAL_MANAGER("일반 관리자"),
    SENIOR_MANAGER("책임 관리자"),
    WAREHOUSE_MANAGER("창고 관리자"),
    FRANCHISE_MANAGER("가맹점 담당자"),
    VENDOR_MANAGER("거래처 담당자"),
    SYSTEM("시스템");

    private final String authorityLabelKo;

    public static MemberAuthorityType of(Integer authorityId) {
        return switch (authorityId) {
            case 1 -> MASTER;
            case 2 -> GENERAL_MANAGER;
            case 3 -> SENIOR_MANAGER;
            case 4 -> WAREHOUSE_MANAGER;
            case 5 -> FRANCHISE_MANAGER;
            case 6 -> VENDOR_MANAGER;
            case 99 -> SYSTEM;
            default -> throw new IllegalArgumentException("Unknown authorityId: " + authorityId);
        };
    }

}
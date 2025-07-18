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
    SYSTEM("시스템"),
    SUPER_ADMIN("최고 관리자");

    private final String authorityLabelKo;

}
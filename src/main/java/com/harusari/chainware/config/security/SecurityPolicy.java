package com.harusari.chainware.config.security;

import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.List;

import static com.harusari.chainware.config.security.AccessType.*;
import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.*;
import static org.springframework.http.HttpMethod.*;

@Getter
@RequiredArgsConstructor
public enum SecurityPolicy {

    /* Member */
    // Role Based
    EMAIL_EXISTS_GET("/api/v1/members/email-exists", GET, ROLE_BASED, List.of(MASTER)), // 이메일 중복 확인
    MEMBER_HEADQUARTERS_POST("/api/v1/members/headquarters", POST, ROLE_BASED, List.of(MASTER)), // 본사 직원 회원가입
    MEMBER_FRANCHISE_POST("/api/v1/members/franchise", POST, ROLE_BASED, List.of(MASTER)), // 가맹점 회원가입
    MEMBER_VENDOR_POST("/api/v1/members/vendor", POST, ROLE_BASED, List.of(MASTER)), // 거래처 회원가입
    MEMBER_WAREHOUSE_POST("/api/v1/members/warehouse", POST, ROLE_BASED, List.of(MASTER)),
    MEMBERS_GET("/api/v1/members", GET, ROLE_BASED, List.of(MASTER)), // 회원 정보 조회
    MEMBERS_DETAIL_GET("/api/v1/members/{memberId}", GET, ROLE_BASED, List.of(MASTER)), // 회원 정보 상세 조회

    // Permit All
    LOGIN_POST("/api/v1/auth/login", POST, PERMIT_ALL, List.of()), // 로그인
    REFRESH_POST("/api/v1/auth/refresh", POST, PERMIT_ALL, List.of()), // 리프레시 토큰 재발급

    // Authenticated
    LOGOUT_POST("/api/v1/auth/logout", POST, AUTHENTICATED, List.of()), // 로그아웃
    PASSWORD_POST("/api/v1/auth/password", POST, AUTHENTICATED, List.of()), // 비밀번호 변경

    /* Product */


    /* Franchise */


    /* Vendor */


    /* Warehouse */


    /* Order */


    /* Requisition */
    REQUISITION_CREATE_POST("/api/v1/requisitions", POST, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 작성

    REQUISITION_SUBMIT_PUT("/api/v1/requisitions/{requisitionId}/submit", PUT, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 상신

    REQUISITION_DELETE("/api/v1/requisitions/{requisitionId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 삭제

    REQUISITION_APPROVE_PUT("/api/v1/requisitions/{requisitionId}/approve", PUT, ROLE_BASED, List.of(SENIOR_MANAGER)), // 품의서 승인

    REQUISITION_REJECT_PUT("/api/v1/requisitions/{requisitionId}/reject", PUT, ROLE_BASED, List.of(SENIOR_MANAGER)), // 품의서 반려

    REQUISITION_GET("/api/v1/requisitions", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 품의서 목록 조회

    REQUISITION_DETAIL_GET("/api/v1/requisitions/{requisitionId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 품의서 상세 조회


    /* Delivery */


    /* Purchase Order */
    PURCHASE_GET("/api/v1/purchases", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, VENDOR_MANAGER)), // 발주 목록 조회

    PURCHASE_DETAIL_GET("/api/v1/purchases/{purchaseOrderId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 상세 조회

    PURCHASE_CANCEL_PUT("/api/v1/purchases/{purchaseOrderId}/cancel", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 취소

    PURCHASE_UPDATE_PUT("/api/v1/purchases/{purchaseOrderId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 수정

    PURCHASE_APPROVE_PUT("/api/v1/purchases/{purchaseOrderId}/approve", PUT, ROLE_BASED, List.of(VENDOR_MANAGER )), // 발주 승인

    PURCHASE_REJECT_PUT("/api/v1/purchases/{purchaseOrderId}/reject", PUT, ROLE_BASED, List.of(VENDOR_MANAGER)); // 발주 거절


    /* Take Back */


    /* Notification */


    /* Disposal */


    /* Statistics */


    private final String path;
    private final HttpMethod method;
    private final AccessType accessType;
    private final List<MemberAuthorityType> memberAuthorities;

}
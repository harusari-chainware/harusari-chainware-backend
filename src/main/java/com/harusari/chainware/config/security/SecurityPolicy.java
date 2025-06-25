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
    WAREHOUSE_UPDATE("/api/v1/warehouse/{warehouseId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 창고 마스터 수정
    WAREHOUSE_DELETE("/api/v1/warehouse/{warehouseId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 창고 마스터 삭제
    WAREHOUSE_INVENTORY_REGISTER("/api/v1/warehouse/{warehouseId}/inventory", POST, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 보유 재고 등록
    WAREHOUSE_INVENTORY_UPDATE("/api/v1/warehouse/inventory/{inventoryId}", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 보유 재고 수정
    WAREHOUSE_INVENTORY_DELETE("/api/v1/warehouse/inventory/{inventoryId}", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 보유 재고 삭제


    /* Order */
    ORDER_CREATE("/api/v1/orders", POST, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 주문 등록
    ORDER_UPDATE("/api/v1/orders/{orderId}", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 주문 수정
    ORDER_CANCEL("/api/v1/orders/{orderId}/cancel", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 주문 수정
    ORDER_APPROVE("/api/v1/orders/{orderId}/approve", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 주문 승인
    ORDER_REJECT("/api/v1/orders/{orderId}/reject", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 주문 반려

    ORDER_LIST_GET("/api/v1/orders", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, FRANCHISE_MANAGER)), // 주문 목록 조회
    ORDER_DETAIL_GET("/api/v1/orders/{orderId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, FRANCHISE_MANAGER)), // 주문 상세 조회


    /* Requisition */


    /* Delivery */
    DELIVERY_START("/api/v1/delivery/{deliveryId}/start", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 배송 시작
    DELIVERY_COMPLETE("/api/v1/delivery/{deliveryId}/complete", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 배송 완료


    /* Purchase Order */


    /* Take Back */


    /* Notification */


    /* Disposal */


    /* Statistics */


    /* Swagger */
    SWAGGER_UI("/swagger-ui/**", GET, PERMIT_ALL, List.of()),
    SWAGGER_RESOURCE("/swagger-resources/**", GET, PERMIT_ALL, List.of()),
    SWAGGER_DOCS("/v3/api-docs/**", GET, PERMIT_ALL, List.of()),
    SWAGGER_CONFIG("/v3/api-docs/swagger-config", GET, PERMIT_ALL, List.of()),
    SWAGGER_WEBJARS("/webjars/**", GET, PERMIT_ALL, List.of());


    private final String path;
    private final HttpMethod method;
    private final AccessType accessType;
    private final List<MemberAuthorityType> memberAuthorities;

}
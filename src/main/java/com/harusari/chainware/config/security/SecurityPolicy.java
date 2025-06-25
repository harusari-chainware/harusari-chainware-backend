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
    LOGIN_HISTORY_GET("/api/v1/members/{memberId}/login-history", GET, ROLE_BASED, List.of(MASTER)),

    // Permit All
    LOGIN_POST("/api/v1/auth/login", POST, PERMIT_ALL, List.of()), // 로그인
    REFRESH_POST("/api/v1/auth/refresh", POST, PERMIT_ALL, List.of()), // 리프레시 토큰 재발급

    // Authenticated
    LOGOUT_POST("/api/v1/auth/logout", POST, AUTHENTICATED, List.of()), // 로그아웃
    PASSWORD_POST("/api/v1/auth/password", POST, AUTHENTICATED, List.of()), // 비밀번호 변경

    /* Category */
    CATEGORY_POST("/api/v1/category", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 카테고리 등록
    CATEGORY_PUT("/api/v1/category/{categoryId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 카테고리 수정
    CATEGORY_DELETE("/api/v1/category/{categoryId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 카테고리 삭제
    TOP_CATEGORY_POST("/api/v1/topcategory", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 상위 카테고리 등록
    TOP_CATEGORY_PUT("/api/v1/topcategory/{topCategoryId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 상위 카테고리 수정
    TOP_CATEGORY_DELETE("/api/v1/topcategory/{topCategoryId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 상위 카테고리 삭제
    CATEGORY_LIST_GET("/api/v1/categories", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 전체 카테고리 목록 조회
    CATEGORY_LIST_BY_TOP_GET("/api/v1/categories/top/{topCategoryId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 특정 상위 카테고리와 카테고리, 제품 목록 조회
    CATEGORY_DETAIL_BY_ID_GET("/api/v1/categories/{categoryId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 특정 카테고리와 상위 카테고리, 제품 목록 조회

    /* Contract */
    CONTRACT_POST("/api/v1/contract", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 별 제품 정보 등록
    CONTRACT_PUT("/api/v1/contract/{contractId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 별 제품 정보 수정
    CONTRACT_DELETE("/api/v1/contract/{contractId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 별 제품 정보 삭제

    // Vendor Product Contract Query (수정 필요)
    VENDOR_CONTRACT_LIST_GET("/api/v1/contracts", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER)), // 모든 거래처-제품 계약 목록 조회
    VENDOR_CONTRACT_LIST_BY_VENDOR_GET("/api/v1/contracts/{vendorId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 특정 거래처-제품 계약 목록 조회

    /* Product */
    PRODUCT_POST("/api/v1/product", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 제품 등록
    PRODUCT_PUT("/api/v1/product/{productId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 제품 수정
    PRODUCT_DELETE("/api/v1/product/{productId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 제품 삭제

    // Product Query (수정 필요)
    PRODUCT_LIST_GET("/api/v1/products", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, FRANCHISE_MANAGER)), // 전체 제품 목록 조회
    PRODUCT_DETAIL_GET("/api/v1/products/{productId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, FRANCHISE_MANAGER)), // 제품 상세 정보 조회

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
    REQUISITION_CREATE_POST("/api/v1/requisitions", POST, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 작성

    REQUISITION_SUBMIT_PUT("/api/v1/requisitions/{requisitionId}/submit", PUT, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 상신

    REQUISITION_DELETE("/api/v1/requisitions/{requisitionId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 삭제

    REQUISITION_APPROVE_PUT("/api/v1/requisitions/{requisitionId}/approve", PUT, ROLE_BASED, List.of(SENIOR_MANAGER)), // 품의서 승인

    REQUISITION_REJECT_PUT("/api/v1/requisitions/{requisitionId}/reject", PUT, ROLE_BASED, List.of(SENIOR_MANAGER)), // 품의서 반려

    REQUISITION_GET("/api/v1/requisitions", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 품의서 목록 조회

    REQUISITION_DETAIL_GET("/api/v1/requisitions/{requisitionId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 품의서 상세 조회


    /* Delivery */
    DELIVERY_START("/api/v1/delivery/{deliveryId}/start", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 배송 시작
    DELIVERY_COMPLETE("/api/v1/delivery/{deliveryId}/complete", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 배송 완료


    /* Purchase Order */
    PURCHASE_GET("/api/v1/purchases", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, VENDOR_MANAGER)), // 발주 목록 조회

    PURCHASE_DETAIL_GET("/api/v1/purchases/{purchaseOrderId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 상세 조회

    PURCHASE_CANCEL_PUT("/api/v1/purchases/{purchaseOrderId}/cancel", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 취소

    PURCHASE_UPDATE_PUT("/api/v1/purchases/{purchaseOrderId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 수정

    PURCHASE_APPROVE_PUT("/api/v1/purchases/{purchaseOrderId}/approve", PUT, ROLE_BASED, List.of(VENDOR_MANAGER )), // 발주 승인

    PURCHASE_REJECT_PUT("/api/v1/purchases/{purchaseOrderId}/reject", PUT, ROLE_BASED, List.of(VENDOR_MANAGER)), // 발주 거절


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
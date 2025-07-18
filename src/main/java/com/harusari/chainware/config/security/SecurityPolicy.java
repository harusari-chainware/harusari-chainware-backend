package com.harusari.chainware.config.security;

import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import static com.harusari.chainware.config.security.AccessType.*;
import static com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType.*;
import static org.springframework.http.HttpMethod.*;

@Getter
public enum SecurityPolicy {

    /* Member */
    // Permit All
    LOGIN_POST("/api/v1/auth/login", POST, PERMIT_ALL, List.of()), // 로그인
    REFRESH_POST("/api/v1/auth/refresh", POST, PERMIT_ALL, List.of()), // 리프레시 토큰 재발급
    HEALTH_CHECK("/api/v1/health/check", GET, PERMIT_ALL, List.of()), // 헬스 체크
    FASTAPI_HEALTH("/api/v1/health/check/fastapi", GET, PERMIT_ALL, List.of()), // fastapi 헬스 체크

    // Authenticated
    LOGOUT_POST("/api/v1/auth/logout", POST, AUTHENTICATED, List.of()), // 로그아웃
    PASSWORD_PUT("/api/v1/members/password", PUT, AUTHENTICATED, List.of()), // 비밀번호 변경
    MEMBERS_ME_GET("/api/v1/members/me", GET, AUTHENTICATED, List.of()), // 회원 정보 조회
    MEMBERS_ME_PUT("/api/v1/members/me", PUT, AUTHENTICATED, List.of()), // 회원 정보 수정

    // Role Based
    EMAIL_EXISTS_GET("/api/v1/members/email-exists", GET, ROLE_BASED, List.of(MASTER)), // 이메일 중복 확인
    MEMBER_HEADQUARTERS_POST("/api/v1/members/headquarters", POST, ROLE_BASED, List.of(MASTER)), // 본사 직원 회원가입
    MEMBER_FRANCHISE_POST("/api/v1/members/franchise", POST, ROLE_BASED, List.of(MASTER)), // 가맹점 회원가입
    MEMBER_VENDOR_POST("/api/v1/members/vendor", POST, ROLE_BASED, List.of(MASTER)), // 거래처 회원가입
    MEMBER_WAREHOUSE_POST("/api/v1/members/warehouse", POST, ROLE_BASED, List.of(MASTER)),
    MEMBERS_GET("/api/v1/members", GET, ROLE_BASED, List.of(MASTER, GENERAL_MANAGER)), // 회원 정보 조회
    MEMBERS_DETAIL_GET("/api/v1/members/{memberId}", GET, ROLE_BASED, List.of(MASTER)), // 회원 정보 상세 조회
    MEMBERS_PUT("/api/v1/members/{memberId}", PUT, ROLE_BASED, List.of(MASTER)), // 회원 정보 수정
    MEMBERS_DELETE("/api/v1/members/{memberId}", DELETE, ROLE_BASED, List.of(MASTER)), // 회원 탈퇴
    LOGIN_HISTORY_GET("/api/v1/members/{memberId}/login-history", GET, ROLE_BASED, List.of(MASTER)), // 로그인 내역 조회

    /* Category */
    CATEGORY_POST("/api/v1/category", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 카테고리 등록
    CATEGORY_PUT("/api/v1/category/{categoryId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 카테고리 수정
    CATEGORY_DELETE("/api/v1/category/{categoryId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 카테고리 삭제
    TOP_CATEGORY_POST("/api/v1/topcategory", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 상위 카테고리 등록
    TOP_CATEGORY_PUT("/api/v1/topcategory/{topCategoryId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 상위 카테고리 수정
    TOP_CATEGORY_DELETE("/api/v1/topcategory/{topCategoryId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 상위 카테고리 삭제
    CATEGORY_LIST_GET("/api/v1/categories", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, FRANCHISE_MANAGER, VENDOR_MANAGER)), // 전체 카테고리 목록 조회
    CATEGORY_LIST_BY_TOP_GET("/api/v1/categories/top/{topCategoryId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, FRANCHISE_MANAGER, VENDOR_MANAGER)), // 특정 상위 카테고리와 카테고리, 제품 목록 조회
    CATEGORY_DETAIL_BY_ID_GET("/api/v1/categories/{categoryId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, FRANCHISE_MANAGER, VENDOR_MANAGER)), // 특정 카테고리와 상위 카테고리, 제품 목록 조회

    /* Contract */
    CONTRACT_POST("/api/v1/contract", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 별 제품 정보 등록
    CONTRACT_PUT("/api/v1/contract/{contractId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 별 제품 정보 수정
    CONTRACT_DELETE("/api/v1/contract/{contractId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 별 제품 정보 삭제
    VENDOR_CONTRACT_LIST_GET("/api/v1/contracts", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, VENDOR_MANAGER)), // 모든 거래처-제품 계약 목록 조회
    VENDOR_CONTRACT_LIST_BY_VENDOR_GET("/api/v1/contracts/{vendorId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, VENDOR_MANAGER)), // 특정 거래처-제품 계약 목록 조회

    /* Product */
    PRODUCT_POST("/api/v1/product", POST, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 제품 등록
    PRODUCT_PUT("/api/v1/product/{productId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 제품 수정
    PRODUCT_DELETE("/api/v1/product/{productId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 제품 삭제
    PRODUCT_LIST_GET("/api/v1/products", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, FRANCHISE_MANAGER, VENDOR_MANAGER)), // 전체 제품 목록 조회
    PRODUCT_DETAIL_GET("/api/v1/products/{productId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, WAREHOUSE_MANAGER, FRANCHISE_MANAGER, VENDOR_MANAGER)), // 제품 상세 정보 조회

    /* Franchise */
    FRANCHISE_PUT("/api/v1/franchises/{franchiseId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 정보 수정
    FRANCHISES_GET("/api/v1/franchises", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 목록 조회
    FRANCHISES_GET_ALL("/api/v1/franchises/all", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 목록 조회
    FRANCHISE_DETAIL_GET("/api/v1/franchises/{franchiseId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 상세 조회,
    FRANCHISE_CONTRACT_INFO_GET("/api/v1/franchises/{franchiseName}/contract-info", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 계약 정보 조회
    FRANCHISE_AGREEMENT_DOWNLOAD_URL("/api/v1/franchises/{franchiseId}/agreement/download", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 계약서 다운로드

    /* Vendor */
    VENDORS_PUT("/api/v1/vendors/{vendorId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 정보 수정
    VENDORS_GET("/api/v1/vendors", GET, ROLE_BASED, List.of(VENDOR_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 목록 조회
    VENDORS_GET_ALL("/api/v1/vendors/all", GET, ROLE_BASED, List.of(VENDOR_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 목록 조회
    VENDOR_DETAIL_GET("/api/v1/vendors/{vendorId}", GET, ROLE_BASED, List.of(VENDOR_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 상세 조회
    VENDOR_CONTRACT_INFO_GET("/api/v1/vendors/{vendorName}/contract-info", GET, ROLE_BASED, List.of(VENDOR_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 거래처 계약 정보 조회
    VENDOR_AGREEMENT_DOWNLOAD_URL("/api/v1/vendors/{vendorId}/agreement/download", GET, ROLE_BASED, List.of(VENDOR_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)),

    /* Warehouse */
    WAREHOUSE_UPDATE("/api/v1/warehouse/{warehouseId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 창고 마스터 수정
    WAREHOUSE_DELETE("/api/v1/warehouse/{warehouseId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 창고 마스터 삭제
    WAREHOUSE_INVENTORY_REGISTER("/api/v1/warehouse/{warehouseId}/inventory", POST, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 보유 재고 등록
    WAREHOUSE_INVENTORY_UPDATE("/api/v1/warehouse/inventory/{inventoryId}", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 보유 재고 수정
    WAREHOUSE_INVENTORY_DELETE("/api/v1/warehouse/inventory/{inventoryId}", DELETE, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 보유 재고 삭제

    WAREHOUSE_LIST_GET("/api/v1/warehouse", GET, ROLE_BASED, List.of(WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 창고 마스터 목록 조회
    WAREHOUSE_MY("/api/v1/warehouse/my", GET, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 창고 마스터 목록 조회
    WAREHOUSE_LIST_GET_ALL("/api/v1/warehouse/all", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 창고 마스터 목록 조회
    WAREHOUSE_DETAIL_GET("/api/v1/warehouse/{warehouseId}", GET, ROLE_BASED, List.of(WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 창고 마스터 상세 조회
    WAREHOUSE_INVENTORY_LIST_GET("/api/v1/warehouse/inventory", GET, ROLE_BASED, List.of(WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 보유 재고 목록 조회
    WAREHOUSE_INVENTORY_DETAIL_GET("/api/v1/warehouse/inventory/{inventoryId}", GET, ROLE_BASED, List.of(WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 보유 재고 상세 조회

    /* Order */
    ORDER_CREATE("/api/v1/orders", POST, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 주문 등록
    ORDER_UPDATE("/api/v1/orders/{orderId}", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 주문 수정
    ORDER_CANCEL("/api/v1/orders/{orderId}/cancel", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 주문 수정
    ORDER_APPROVE("/api/v1/orders/{orderId}/approve", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 주문 승인
    ORDER_REJECT("/api/v1/orders/{orderId}/reject", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 주문 반려
    ORDER_AVAILABLE_WAREHOUSE("/api/v1/orders/{orderId}/available-warehouses", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 주문 가능 창고 조회
    ORDER_LIST_GET("/api/v1/orders", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, FRANCHISE_MANAGER)), // 주문 목록 조회
    ORDER_DETAIL_GET("/api/v1/orders/{orderId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, FRANCHISE_MANAGER)), // 주문 상세 조회
    ORDER_OWNER_GET("/api/v1/orders/my-franchise", GET, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 담당자의 가맹점 정보 조회

    /* Requisition */
    REQUISITION_CREATE_POST("/api/v1/requisitions", POST, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 작성
    REQUISITION_SUBMIT_PUT("/api/v1/requisitions/{requisitionId}/submit", PUT, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 상신
    REQUISITION_DELETE("/api/v1/requisitions/{requisitionId}", DELETE, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 삭제
    REQUISITION_UPDATE("/api/v1/requisitions/{requisitionId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER)), // 품의서 수정
    REQUISITION_APPROVE_PUT("/api/v1/requisitions/{requisitionId}/approve", PUT, ROLE_BASED, List.of(SENIOR_MANAGER)), // 품의서 승인
    REQUISITION_REJECT_PUT("/api/v1/requisitions/{requisitionId}/reject", PUT, ROLE_BASED, List.of(SENIOR_MANAGER)), // 품의서 반려
    REQUISITION_GET("/api/v1/requisitions", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 품의서 목록 조회
    REQUISITION_DETAIL_GET("/api/v1/requisitions/{requisitionId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 품의서 상세 조회

    /* Delivery */
    DELIVERY_START("/api/v1/delivery/{deliveryId}/start", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 배송 시작
    DELIVERY_COMPLETE("/api/v1/delivery/{deliveryId}/complete", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 배송 완료
    DELIVERY_LIST_GET("/api/v1/delivery", GET, ROLE_BASED, List.of(FRANCHISE_MANAGER, WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 배송 목록 조회
    DELIVERY_DETAIL_GET("/api/v1/delivery/{deliveryId}", GET, ROLE_BASED, List.of(FRANCHISE_MANAGER, WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 배송 상세 조회

    /* Purchase Order */
    PURCHASE_GET("/api/v1/purchases", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, VENDOR_MANAGER, WAREHOUSE_MANAGER)), // 발주 목록 조회
    PURCHASE_DETAIL_GET("/api/v1/purchases/{purchaseOrderId}", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER, VENDOR_MANAGER, WAREHOUSE_MANAGER)), // 발주 상세 조회
    PURCHASE_CANCEL_PUT("/api/v1/purchases/{purchaseOrderId}/cancel", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 취소
    PURCHASE_UPDATE_PUT("/api/v1/purchases/{purchaseOrderId}", PUT, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주 수정
    PURCHASE_APPROVE_PUT("/api/v1/purchases/{purchaseOrderId}/approve", PUT, ROLE_BASED, List.of(VENDOR_MANAGER)), // 발주 승인
    PURCHASE_REJECT_PUT("/api/v1/purchases/{purchaseOrderId}/reject", PUT, ROLE_BASED, List.of(VENDOR_MANAGER)), // 발주 거절
    PURCHASE_SHIPPED_PUT("/api/v1/purchases/{purchaseOrderId}/shipped", PUT, ROLE_BASED, List.of(VENDOR_MANAGER)), // 출고 완료 처리
    PURCHASE_PURCHASE_INBOUND("/api/v1/purchases/{purchaseOrderId}/inbound", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 입고 완료 처리

    /* Take Back */
    TAKEBACK_REGISTER("/api/v1/takeback", POST, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 반품 신청
    TAKEBACK_CANCEL("/api/v1/takeback/{takebackId}/cancel", PUT, ROLE_BASED, List.of(FRANCHISE_MANAGER)), // 반품 취소
    TAKEBACK_WAREHOUSED("/api/v1/takeback/{takebackId}/collect", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 반품 수거
    TAKEBACK_APPROVE("/api/v1/takeback/{takebackId}/approve", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 반품 승인
    TAKEBACK_REJECT("/api/v1/takeback/{takebackId}/reject", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 반품 반려
    TAKEBACK_DISPOSAL("/api/v1/takeback/{takebackId}/disposal", PUT, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 반품 폐기
    TAKEBACK_LIST_GET("/api/v1/takeback", GET, ROLE_BASED, List.of(FRANCHISE_MANAGER, WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 반품 목록 조회
    TAKEBACK_DETAIL_GET("/api/v1/takeback/{takebackId}", GET, ROLE_BASED, List.of(FRANCHISE_MANAGER, WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 반품 상세 조회

    /* Disposal */
    DISPOSAL_CREATE("/api/v1/disposal", POST, ROLE_BASED, List.of(FRANCHISE_MANAGER, WAREHOUSE_MANAGER)), // 폐기 등록
    DISPOSAL_GET("/api/v1/disposal", GET, ROLE_BASED, List.of(FRANCHISE_MANAGER, WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 폐기 조회
    DISPOSAL_GET_SEARCH("/api/v1/disposal/search", GET, ROLE_BASED, List.of(FRANCHISE_MANAGER, WAREHOUSE_MANAGER, GENERAL_MANAGER, SENIOR_MANAGER)), // 폐기 상품 조회
    DISPOSAL_GET_TAKE_BACK_SEARCH("/api/v1/disposal/takebacks/search", GET, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 반품 조회
    DISPOSAL_GET_TAKEBACK("/api/v1/disposal/products/takeback/{takeBackId}", GET, ROLE_BASED, List.of(WAREHOUSE_MANAGER)), // 반품 상품 조회

    /* Statistics */
    STATISTICS_DISPOSAL_RATE("/api/v1/statistics/disposal-rate", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 폐기율 조회
    STATISTICS_DISPOSAL_RATE_TREND_GROUP("/api/v1/statistics/disposal-rate/trend", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 폐기율 조회
    STATISTICS_INVENTORY_TURNOVER_RATE("/api/v1/statistics/inventory-turnover", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 재고 회전율 조회
    STATISTICS_INVENTORY_TURNOVER_RATE_TREND("/api/v1/statistics/inventory-turnover/trend", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 재고 회전율 조회
    STATISTICS_MENU_SALES("/api/v1/statistics/menu-sales", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 메뉴별 매출 통계 조회
    STATISTICS_PURCHASE_ORDER("/api/v1/statistics/purchase-order", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주량 통계 조회
    STATISTICS_PURCHASE_ORDER_TREND("/api/v1/statistics/purchase-order/trend", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 발주량 통계 조회
    STATISTICS_SALES_PATTERNS("/api/v1/statistics/patterns", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 매출 패턴 통계 조회
    STATISTICS_STORE_ORDER("/api/v1/statistics/store-order", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 주문량 조회
    STATISTICS_STORE_ORDER_TREND("/api/v1/statistics/store-order/trend", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 가맹점 주문량 조회
    STATISTICS_TOTAL_SALES("/api/v1/statistics/total-sales", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 총 매출 통계 조회
    STATISTICS_PREDICTION_COMPARISON("/api/v1/statistics/prediction-comparison", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 예측 조회
    PREDICTION_ACCURACY("/api/v1/accuracy/summary", GET, ROLE_BASED, List.of(GENERAL_MANAGER, SENIOR_MANAGER)), // 예측 결과값 조회

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

    SecurityPolicy(String path, HttpMethod method, AccessType accessType, List<MemberAuthorityType> authorities) {
        this.path = path;
        this.method = method;
        this.accessType = accessType;

        if (accessType == ROLE_BASED && !authorities.contains(SUPER_ADMIN)) {
            List<MemberAuthorityType> updated = new ArrayList<>(authorities);
            updated.add(SUPER_ADMIN);
            this.memberAuthorities = List.copyOf(updated);
        } else {
            this.memberAuthorities = authorities;
        }
    }

}
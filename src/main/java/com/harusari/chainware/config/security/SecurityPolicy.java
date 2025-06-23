package com.harusari.chainware.config.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityPolicy {

    protected static final String[] PUBLIC_URLS = {
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
    };

    protected static final String[] MASTER_ONLY_URLS = {
            "/api/v1/members/email-exists",
            "/api/v1/members/headquarters",
            "/api/v1/members/franchise",
            "/api/v1/members/vendor",
            "/api/v1/members/warehouse"
    };

    protected static final String[] GENERAL_MANAGER_URLS = {
            "/api/v1/statistics/disposal-rate",
            "/api/v1/statistics/inventory-turnover",
            "/api/v1/statistics/menu-sales",
            "/api/v1/statistics/purchase-order",
            "/api/v1/statistics/patterns",
            "/api/v1/statistics/store-order",
            "/api/v1/statistics/total-sales"


    };

    protected static final String[] SENIOR_MANAGER_URLS = {
            "/api/v1/statistics/disposal-rate",
            "/api/v1/statistics/inventory-turnover",
            "/api/v1/statistics/menu-sales",
            "/api/v1/statistics/purchase-order",
            "/api/v1/statistics/patterns",
            "/api/v1/statistics/store-order",
            "/api/v1/statistics/total-sales"

    };

    protected static final String[] WAREHOUSE_MANAGER_URLS = {

    };

    protected static final String[] FRANCHISE_MANAGER_URLS = {

    };

    protected static final String[] VENDOR_MANAGER_URLS = {

    };

    protected static final String[] AUTHENTICATED_URLS = {
            "/api/v1/auth/logout",
            "/api/v1/requisitions/**",
            "/api/v1/orders/**",
            "/api/v1/delivery/**",
            "/api/v1/warehouse/**",
    };

}
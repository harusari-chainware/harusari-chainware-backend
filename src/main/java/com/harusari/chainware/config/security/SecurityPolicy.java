package com.harusari.chainware.config.security;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityPolicy {

    protected static final String[] PUBLIC_URLS = {
            // member
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
    };

    protected static final String[] MASTER_ONLY_URLS = {
            // member
            "/api/v1/members/email-exists",
            "/api/v1/members/headquarters",
            "/api/v1/members/franchise",
            "/api/v1/members/vendor",
            "/api/v1/members/warehouse",
            "/api/v1/members",
            "/api/v1/members/{memberId}",
    };

    protected static final String[] GENERAL_MANAGER_URLS = {
            // orders
            "/api/v1/orders/{orderId}/approvePurchaseOrder",
            "/api/v1/orders/{orderId}/reject",

            // warehouse
            "/api/v1/warehouse/{warehouseId}",

            // requisition
            "/api/v1/requisitions",
            "/api/v1/requisitions/create",
            "/api/v1/requisitions/{requisitionID}",
            "/api/v1/requisitions/{requisitionId}/submit",

            // purchase
            "/api/v1/purchases",
            "/api/v1/purchases/{purchaseOrderId}",

            // statistics
            "/api/v1/statistics/disposal-rate",
            "/api/v1/statistics/inventory-turnover",
            "/api/v1/statistics/menu-sales",
            "/api/v1/statistics/purchase-order",
            "/api/v1/statistics/patterns",
            "/api/v1/statistics/store-order",
            "/api/v1/statistics/total-sales",
            "/api/v1/requisitions",
            "/api/v1/requisitions/create",
            "/api/v1/requisitions/{requisitionID}/submit",
            "/api/v1/requisitions/{requisitionID}",
            "/api/v1/purchases",
            "/api/v1/purchases/{purchaseOrderId}",
            "/api/v1/purchases/{purchaseOrderId}/cancel",


            "/api/v1/statistics/total-sales",
    };

    protected static final String[] SENIOR_MANAGER_URLS = {
            // orders
            "/api/v1/orders/{orderId}/approvePurchaseOrder",
            "/api/v1/orders/{orderId}/reject",

            // warehouse
            "/api/v1/warehouse/{warehouseId}",

            // requisitions
            "/api/v1/requisitions",
            "/api/v1/requisitions/{requisitionID}",
            "/api/v1/requisitions/{requisitionId}/approvePurchaseOrder",
            "/api/v1/requisitions/{requisitionId}/reject",

            // purchases
            "/api/v1/purchases",
            "/api/v1/purchases/{purchaseOrderId}",

            // vendors
            "/api/v1/vendors/{vendorId}",

            // statistics
            "/api/v1/statistics/disposal-rate",
            "/api/v1/statistics/inventory-turnover",
            "/api/v1/statistics/menu-sales",
            "/api/v1/statistics/purchase-order",
            "/api/v1/statistics/patterns",
            "/api/v1/statistics/store-order",
            "/api/v1/statistics/total-sales",
            "/api/v1/orders/{orderId}/approvePurchaseOrder",
            "/api/v1/orders/{orderId}/reject",
            "/api/v1/requisitions/create",
            "/api/v1/requisitions/{requisitionId}/submit",
            "/api/v1/requisitions",
            "/api/v1/requisitions/{requisitionID}",
            "/api/v1/purchases",
            "/api/v1/purchases/{purchaseOrderId}",
            "/api/v1/purchases/{purchaseOrderId}/cancel",
            "/api/v1/vendors/{vendorId}"
    };

    protected static final String[] WAREHOUSE_MANAGER_URLS = {
            // delivery
            "/api/v1/delivery/{deliveryId}/start",

            // warehouse
            "/api/v1/warehouse/{warehouseId}/inventory",
            "/api/v1/warehouse/inventory/{inventoryId}",
    };

    protected static final String[] FRANCHISE_MANAGER_URLS = {
            // orders
            "/api/v1/orders",
            "/api/v1/orders/{orderId}",
            "/api/v1/orders/{orderId}/cancel",

            // delivery
            "/api/v1/delivery/{deliveryId}/complete",
    };

    protected static final String[] VENDOR_MANAGER_URLS = {
            // member
            "/api/v1/auth/logout",
            "/api/v1/members/password",
            "/api/v1/purchases/{purchaseOrderId}/approvePurchaseOrder",
            "/api/v1/purchases/{purchaseOrderId}/reject",

            // requisitions
            "/api/v1/requisitions/**",
    };

    protected static final String[] AUTHENTICATED_URLS = {
            "/api/v1/auth/logout",
            "/api/v1/members/password",
            "/api/v1/requisitions/**",
    };

}
package com.harusari.chainware.purchase.query.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseOrderDetailResponse {

    private PurchaseOrderInfo purchaseOrderInfo;

    private MemberInfo drafter;
    private VendorInfo vendor;
    private PWarehouseInfo warehouse;

    private List<PurchaseOrderProductResponse> products;
}
package com.harusari.chainware.order.query.dto.response;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public class OrderProductInfo {
    private String productCode;
    private String productName;
    private String unit;
    private String storageType;
    private long contractPrice;
    private int quantity;
    private long amount;
}

package com.harusari.chainware.delivery.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryProductInfo {
    private String productCode;
    private String productName;
    private String unitQuantity;
    private String unitSpec;
    private String storeType;
    private Integer unitPrice;
    private Integer quantity;
    private Long amount;
}

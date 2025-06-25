package com.harusari.chainware.order.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductInfo {
    private String productCode;
    private String productName;
    private String unitQuantity;
    private String unitSpec;
    private String storageType;
    private Integer contractPrice;
    private Integer quantity;
    private Long amount;
}

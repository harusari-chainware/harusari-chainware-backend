package com.harusari.chainware.order.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RiskyProductInfo {
    private Long productId;
    private String productName;
    private String productCode;
    private int safetyQuantity;
    private int availableQuantity; // quantity - reservedQuantity
    private int orderedQuantity;
}

package com.harusari.chainware.purchase.query.dto;

public record PurchaseOrderProductResponse(
    Long productId,
    String productCode,
    String productName,
    int quantity,
    int unitPrice,
    int moq
) {}
package com.harusari.chainware.purchase.query.dto;

public record PurchaseOrderProductResponse(
    Long productId,
    String productName,
    int quantity,
    int unitPrice
) {}
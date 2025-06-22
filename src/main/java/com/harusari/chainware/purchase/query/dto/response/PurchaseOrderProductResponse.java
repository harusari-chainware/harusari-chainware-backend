package com.harusari.chainware.purchase.query.dto.response;

public record PurchaseOrderProductResponse(
    Long productId,
    String productName,
    int quantity,
    int unitPrice
) {}
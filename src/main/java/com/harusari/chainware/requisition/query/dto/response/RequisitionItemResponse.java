package com.harusari.chainware.requisition.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequisitionItemResponse {
    private Long requisitionDetailId;
    private Long contractId;
    private Long productId;
    private int quantity;
    private Long unitPrice;
    private Long totalPrice;
}

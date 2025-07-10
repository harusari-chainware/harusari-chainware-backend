package com.harusari.chainware.requisition.query.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequisitionItemInfo {
    private Long requisitionDetailId;
    private Long productId;
    private String productName;
    private String productCode;
    private int moq;

    private int quantity;
    private Long unitPrice;
    private Long totalPrice;
}

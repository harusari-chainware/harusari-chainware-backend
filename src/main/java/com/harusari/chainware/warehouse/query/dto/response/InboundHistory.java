package com.harusari.chainware.warehouse.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InboundHistory {
    private String vendorName;
    private String purchaseOrderCode;
    private Long purchaseOrderId;
    private Integer quantity;
    private LocalDate expirationDate;
    private LocalDateTime inboundedAt;
}

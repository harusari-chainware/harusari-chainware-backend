package com.harusari.chainware.purchase.query.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PurchaseOrderSearchCondition {
    private String status;
    private Long requesterId;
    private LocalDate startDate;
    private LocalDate endDate;
    private int offset = 0;
    private int size = 10;
}

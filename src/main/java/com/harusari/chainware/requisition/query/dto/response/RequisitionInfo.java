package com.harusari.chainware.requisition.query.dto.response;

import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RequisitionInfo {
    private Long requisitionId;
    private String requisitionCode;
    private RequisitionStatus requisitionStatus;

    private Long warehouseId;
    private int productCount;
    private int totalQuantity;
    private Long totalPrice;

    private String rejectReason;
    private LocalDate dueDate;

    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime modifiedAt;
}
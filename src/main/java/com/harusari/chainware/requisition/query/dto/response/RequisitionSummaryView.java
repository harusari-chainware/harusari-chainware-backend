package com.harusari.chainware.requisition.query.dto.response;

import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RequisitionSummaryView {
    private Long requisitionId;
    private String requisitionCode;
    private String vendorName;
    private String drafterName;   // 작성자
    private String approverName;  // 결재자
    private int productCount;
    private int totalQuantity;
    private Long totalPrice;
    private RequisitionStatus requisitionStatus;
    private LocalDate submittedAt;
    private LocalDate reviewedAt;
}
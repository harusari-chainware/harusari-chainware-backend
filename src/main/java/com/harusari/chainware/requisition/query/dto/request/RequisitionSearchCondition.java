package com.harusari.chainware.requisition.query.dto.request;

import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RequisitionSearchCondition {

    private String drafterName;
    private String approverName;
    private RequisitionStatus status;
    private String vendorName;

    private LocalDate createdFrom;
    private LocalDate createdTo;

    private int page = 0;
    private int size = 10;

    private boolean approverView = false;

    public int getOffset() {
        return page * size;
    }
}

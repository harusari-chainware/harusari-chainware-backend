package com.harusari.chainware.requisition.query.dto.request;

import com.harusari.chainware.requisition.command.domain.aggregate.RequisitionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequisitionSearchCondition {

    private RequisitionStatus status;
    private int page = 0;
    private int size = 10;

    private boolean approverView = false;

    public int getOffset() {
        return page * size;
    }
}

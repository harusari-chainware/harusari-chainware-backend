package com.harusari.chainware.requisition.query.dto.response;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RequisitionListResponse {
    private List<RequisitionSummaryView> contents;
    private Pagination pagination;
}

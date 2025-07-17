package com.harusari.chainware.purchase.query.dto;

import com.harusari.chainware.common.dto.Pagination;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class PurchaseOrderListResponse {
    private List<PurchaseOrderSummaryResponse> contents;
    private Pagination pagination;
}


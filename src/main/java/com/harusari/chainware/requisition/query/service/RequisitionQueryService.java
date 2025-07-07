package com.harusari.chainware.requisition.query.service;

import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionSummaryResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionSummaryView;

import java.util.List;

public interface RequisitionQueryService {

    List<RequisitionSummaryView> getMyRequisitions(Long memberId, RequisitionSearchCondition condition);

    RequisitionDetailResponse getRequisitionDetail(Long memberId, Long requisitionId);
}

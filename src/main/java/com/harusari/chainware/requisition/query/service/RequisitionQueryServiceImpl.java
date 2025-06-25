package com.harusari.chainware.requisition.query.service;

import com.harusari.chainware.exception.requisition.RequisitionErrorCode;
import com.harusari.chainware.exception.requisition.RequisitionException;
import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionItemResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionSummaryResponse;
import com.harusari.chainware.requisition.query.mapper.RequisitionQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequisitionQueryServiceImpl implements RequisitionQueryService {

    private final RequisitionQueryMapper requisitionQueryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RequisitionSummaryResponse> getMyRequisitions(Long memberId, RequisitionSearchCondition condition) {
        return requisitionQueryMapper.findMyRequisitions(memberId, condition);
    }

    @Override
    @Transactional(readOnly = true)
    public RequisitionDetailResponse getRequisitionDetail(Long memberId, Long requisitionId) {
        RequisitionDetailResponse detail = requisitionQueryMapper.findRequisitionById(requisitionId, memberId);
        if (detail == null) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND);
        }

        List<RequisitionItemResponse> items = requisitionQueryMapper.findItemsByRequisitionId(requisitionId);
        detail.setItems(items);
        return detail;
    }
}

package com.harusari.chainware.requisition.query.service;

import com.harusari.chainware.common.dto.Pagination;
import com.harusari.chainware.exception.requisition.RequisitionErrorCode;
import com.harusari.chainware.exception.requisition.RequisitionException;
import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.*;
import com.harusari.chainware.requisition.query.mapper.RequisitionQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RequisitionQueryServiceImpl implements RequisitionQueryService {

    private final RequisitionQueryMapper requisitionQueryMapper;

    @Override
    public RequisitionListResponse getMyRequisitions(Long memberId, RequisitionSearchCondition condition) {
        List<RequisitionSummaryView> contents = requisitionQueryMapper.findMyRequisitions(memberId, condition);
        int totalCount = requisitionQueryMapper.countMyRequisitions(memberId, condition);

        Pagination pagination = Pagination.of(
                condition.getPage(),
                condition.getSize(),
                totalCount
        );

        return RequisitionListResponse.builder()
                .contents(contents)
                .pagination(pagination)
                .build();
    }

    @Override
    public RequisitionDetailResponse getRequisitionDetail(Long memberId, Long requisitionId) {
        // 1. 메인 정보 조회 (중첩 매핑 DTO)
        RequisitionDetailResponse detail = requisitionQueryMapper.findRequisitionById(requisitionId, memberId);
        if (detail == null) {
            throw new RequisitionException(RequisitionErrorCode.REQUISITION_NOT_FOUND);
        }

        // 2. 품목 리스트 조회 후 추가
        List<RequisitionItemInfo> items = requisitionQueryMapper.findItemsByRequisitionId(requisitionId);
        detail.setItems(items);

        return detail;
    }
}
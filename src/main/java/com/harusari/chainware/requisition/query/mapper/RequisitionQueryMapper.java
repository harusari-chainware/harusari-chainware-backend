package com.harusari.chainware.requisition.query.mapper;

import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionItemResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionSummaryResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RequisitionQueryMapper {

    List<RequisitionSummaryResponse> findMyRequisitions(
        @Param("memberId") Long memberId,
        @Param("condition") RequisitionSearchCondition condition
    );

    RequisitionDetailResponse findRequisitionById(
        @Param("requisitionId") Long requisitionId,
        @Param("memberId") Long memberId
    );

    List<RequisitionItemResponse> findItemsByRequisitionId(@Param("requisitionId") Long requisitionId);
}

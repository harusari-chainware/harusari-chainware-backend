package com.harusari.chainware.requisition.query.mapper;

import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RequisitionQueryMapper {

    List<RequisitionSummaryView> findMyRequisitions(
            @Param("memberId") Long memberId,
            @Param("condition") RequisitionSearchCondition condition
    );

    int countMyRequisitions(@Param("memberId") Long memberId,
                            @Param("condition") RequisitionSearchCondition condition);


    // resultMap으로 중첩 구조 반환
    RequisitionDetailResponse findRequisitionById(
            @Param("requisitionId") Long requisitionId,
            @Param("memberId") Long memberId
    );

    // 반환 타입을 새로운 DTO로
    List<RequisitionItemInfo> findItemsByRequisitionId(@Param("requisitionId") Long requisitionId);
}
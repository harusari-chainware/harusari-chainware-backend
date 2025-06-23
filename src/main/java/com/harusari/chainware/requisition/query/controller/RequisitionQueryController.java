package com.harusari.chainware.requisition.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionSummaryResponse;
import com.harusari.chainware.requisition.query.service.RequisitionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requisitions")
@RequiredArgsConstructor
public class RequisitionQueryController {

    private final RequisitionQueryService requisitionQueryService;

    // 품의서 목록 조회 ( 권한 별로 다르게 조회 )
    @GetMapping
    public List<RequisitionSummaryResponse> getMyRequisitions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute RequisitionSearchCondition condition
    ) {
        Long memberId = userDetails.getMemberId();

        boolean isApprove = userDetails.getMemberAuthorityType() == MemberAuthorityType.SENIOR_MANAGER;
        condition.setApproverView(isApprove);

        return requisitionQueryService.getMyRequisitions(memberId, condition);
    }
    // 품의서 상세 조회
    @GetMapping("/{requisitionId}")
    public RequisitionDetailResponse getRequisitionDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long requisitionId
    ) {
        return requisitionQueryService.getRequisitionDetail(userDetails.getMemberId(), requisitionId);
    }
}

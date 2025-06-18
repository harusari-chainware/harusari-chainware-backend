package com.harusari.chainware.requisition.query.controller;

import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionSummaryResponse;
import com.harusari.chainware.requisition.query.service.RequisitionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/requisitions")
@RequiredArgsConstructor
public class RequisitionQueryController {

    private final RequisitionQueryService requisitionQueryService;

/*    // 🔍 내 품의서 목록 조회
    @GetMapping("/my")
    public List<RequisitionSummaryResponse> getMyRequisitions(
            @AuthenticationPrincipal CustomUserDetails userDetails
            @ModelAttribute RequisitionSearchCondition condition
    ) {
        return requisitionQueryService.getMyRequisitions(memberId, condition);
    }

    // 📄 품의서 상세 조회
    @GetMapping("/{id}")
    public RequisitionDetailResponse getRequisitionDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails
            @PathVariable Long id
    ) {
        return requisitionQueryService.getRequisitionDetail(memberId, id);
    }

    @GetMapping("/my")
    public List<RequisitionSummaryResponse> getMyRequisitions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute RequisitionSearchCondition condition
    ) {
        Long memberId = userDetails.getMemberId();

        // 예: "ROLE_APPROVER"라는 권한이 있는 경우 approverView로 간주
        boolean isApprover = userDetails.hasRole("ROLE_APPROVER");
        condition.setApproverView(isApprover);

        return requisitionQueryService.getMyRequisitions(memberId, condition);
    }

    */


    @GetMapping("/{id}/test")
    public RequisitionDetailResponse getRequisitionDetailTest(
            @RequestParam(name = "memberId") Long memberId,
            @PathVariable Long id
    ) {
        return requisitionQueryService.getRequisitionDetail(memberId, id);
    }

    @GetMapping("/my")
    public List<RequisitionSummaryResponse> getMyRequisitionsTest(
            @RequestParam Long memberId,
            @ModelAttribute RequisitionSearchCondition condition
    ) {
        return requisitionQueryService.getMyRequisitions(memberId, condition);
    }


    @GetMapping("/my/test")
    public List<RequisitionSummaryResponse> getMyRequisitionsTest(
            @RequestParam Long memberId,
            @RequestParam(defaultValue = "false") boolean approverView,
            @ModelAttribute RequisitionSearchCondition condition
    ) {
        condition.setApproverView(approverView);
        return requisitionQueryService.getMyRequisitions(memberId, condition);
    }

}

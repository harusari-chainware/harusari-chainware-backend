package com.harusari.chainware.requisition.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.member.command.domain.aggregate.MemberAuthorityType;
import com.harusari.chainware.requisition.query.dto.request.RequisitionSearchCondition;
import com.harusari.chainware.requisition.query.dto.response.RequisitionDetailResponse;
import com.harusari.chainware.requisition.query.dto.response.RequisitionSummaryResponse;
import com.harusari.chainware.requisition.query.service.RequisitionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "품의 목록 조회", description = "품의 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "품의 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<List<RequisitionSummaryResponse>>> getMyRequisitions(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute RequisitionSearchCondition condition
    ) {
        Long memberId = userDetails.getMemberId();

        boolean isApprove = userDetails.getMemberAuthorityType() == MemberAuthorityType.SENIOR_MANAGER;
        condition.setApproverView(isApprove);

        List<RequisitionSummaryResponse> result = requisitionQueryService.getMyRequisitions(memberId, condition);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(result));
    }

    // 품의서 상세 조회
    @GetMapping("/{requisitionId}")
    @Operation(summary = "품의 상세 조회", description = "선택한 품의서의 상세 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "품의 상세 조회 성공")
    })
    public ResponseEntity<ApiResponse<RequisitionDetailResponse>> getRequisitionDetail(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long requisitionId
    ) {
        RequisitionDetailResponse detail = requisitionQueryService.getRequisitionDetail(userDetails.getMemberId(), requisitionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(detail));
    }
}

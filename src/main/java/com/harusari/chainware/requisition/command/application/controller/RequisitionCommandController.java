package com.harusari.chainware.requisition.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.purchase.command.application.service.PurchaseOrderCommandService;
import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.dto.request.UpdateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.service.RequisitionCommandService;
import com.harusari.chainware.requisition.command.domain.aggregate.RejectRequisitionRequest;
import io.swagger.v3.oas.annotations.Operation;
import com.harusari.chainware.common.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/requisitions")
@RequiredArgsConstructor
public class RequisitionCommandController {

    private final RequisitionCommandService requisitionCommandService;
    private final PurchaseOrderCommandService purchaseOrderCommandService;

    @PostMapping("/create")
    @Operation(summary = "품의서 저장 (임시)", description = "품의서를 저장합니다. 결재자는 필수이며, 품목 리스트는 최소 1개 이상이어야 합니다.")
    public ResponseEntity<ApiResponse<Long>> createRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @Valid @RequestBody CreateRequisitionRequest request
    ) {
        Long requisitionId = requisitionCommandService.createRequisition(userDetail.getMemberId(), request);
        return ResponseEntity.ok(ApiResponse.success(requisitionId));
    }


    @PutMapping("/{requisitionId}/submit")
    @Operation(summary = "품의서 상신", description = "로그인한 사용자가 작성한 임시 저장 품의서를 상신한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "상신 완료")
    public ResponseEntity<ApiResponse<Void>> submitRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable @NotNull Long requisitionId
    ) {
        requisitionCommandService.submitRequisition(userDetail.getMemberId(), requisitionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{requisitionId}/approve")
    @Operation(summary = "품의서 승인", description = "결재자로 입력된 책임 관리자가 품의서를 승인한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "품의서 승인 완료")
    public ResponseEntity<ApiResponse<Void>> approveRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long requisitionId
    ) {
        requisitionCommandService.approveRequisition(requisitionId, userDetail.getMemberId());
        purchaseOrderCommandService.createFromRequisition(requisitionId, userDetail.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{requisitionId}/reject")
    @Operation(summary = "품의서 반려", description = "결재자로 입력된 책임 관리자가 품의서를 반려한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "품의서 반려됨")
    public ResponseEntity<ApiResponse<Void>> rejectRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long requisitionId,
            @RequestBody RejectRequisitionRequest request
    ) {
        requisitionCommandService.rejectRequisition(userDetail.getMemberId(), requisitionId, request);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{requisitionId}")
    @Operation(summary = "품의서 삭제", description = "임시 저장 또는 상신 상태의 품의서를 삭제한다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "품의서 삭제 완료")
    public ResponseEntity<ApiResponse<Void>> deleteRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long requisitionId
    ) {
        requisitionCommandService.deleteRequisition(userDetails.getMemberId(), requisitionId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/{requisitionId}")
    @Operation(summary = "품의서 수정", description = "작성자가 SUBMITTED 이전 상태의 품의서를 수정합니다.")
    public ResponseEntity<ApiResponse<Void>> updateRequisition(
            @PathVariable Long requisitionId,
            @RequestBody @Valid UpdateRequisitionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        requisitionCommandService.updateRequisition(requisitionId, request, userDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

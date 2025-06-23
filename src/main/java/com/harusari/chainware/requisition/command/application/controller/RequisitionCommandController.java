package com.harusari.chainware.requisition.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.purchase.command.application.service.PurchaseOrderCommandService;
import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.service.RequisitionCommandService;
import com.harusari.chainware.requisition.command.domain.aggregate.RejectRequisitionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "품의서 저장 (임시)", description = "품의서를 저장합니다. 결재자는 필수이며, 품목 리스트는 최소 1개 이상이어야 합니다.")
    @ApiResponse(responseCode = "200", description = "품의서가 성공적으로 저장되었습니다.")
    @PostMapping("/create")
    public ResponseEntity<Long> createRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @Valid @RequestBody CreateRequisitionRequest request
    ) {
        System.out.println("현재 로그인 사용자 ID: " + userDetail.getMemberId());
        Long requisitionId = requisitionCommandService.createRequisition(userDetail.getMemberId(), request);
        return ResponseEntity.ok(requisitionId);
    }


    @Operation(summary = "품의서 상신", description = "로그인한 사용자가 작성한 임시 저장 품의서를 상신한다.")
    @ApiResponse(responseCode = "200", description = "상신 완료")
    @PutMapping("/{requisitionId}/submit")
    public ResponseEntity<Void> submitRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable @NotNull Long requisitionId
    ) {
        Long memberId = userDetail.getMemberId();
        System.out.println("현재 로그인 사용자 ID: " + memberId);
        requisitionCommandService.submitRequisition(memberId, requisitionId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "품의서 승인", description = "결재자로 입력된 책임 관리자가 품의서를 승인한다.")
    @ApiResponse(responseCode = "200", description = "품의서 승인 완료")
    @PutMapping("/{requisitionId}/approve")
    public ResponseEntity<Void> approveRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long requisitionId
    ) {
        System.out.println("현재 로그인 사용자 ID: " + userDetail.getMemberId());

        requisitionCommandService.approveRequisition(requisitionId, userDetail.getMemberId());
        purchaseOrderCommandService.createFromRequisition(requisitionId, userDetail.getMemberId());
        
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "품의서 반려", description = "결재자로 입력된 책임 관리자가 품의서를 반려한다.")
    @PutMapping("/{requisitionId}/reject")
    public ResponseEntity<Void> rejectRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable Long requisitionId,
            @RequestBody RejectRequisitionRequest request
    ) {
        requisitionCommandService.rejectRequisition(userDetail.getMemberId(), requisitionId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{requisitionId}")
    public void deleteRequisition(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @PathVariable Long requisitionId
    ) {
        requisitionCommandService.deleteRequisition(userDetails.getMemberId(), requisitionId);
    }

}

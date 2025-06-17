package com.harusari.chainware.requisition.command.application.controller;

import com.harusari.chainware.requisition.command.application.dto.request.CreateRequisitionRequest;
import com.harusari.chainware.requisition.command.application.service.RequisitionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/requisitions")
@RequiredArgsConstructor
public class RequisitionCommandController {

    private final RequisitionCommandService requisitionCommandService;

/*
    @Operation(summary = "품의서 저장 (임시)", description = "품의서를 저장합니다. 결재자는 필수이며, 품목 리스트는 최소 1개 이상이어야 합니다.")
    @ApiResponse(responseCode = "200", description = "품의서가 성공적으로 저장되었습니다.")
    @PostMapping("/create")
    public ResponseEntity<Long> createRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,

            @Valid @RequestBody CreateRequisitionRequest request
    ) {
        Long requisitionId = requisitionCommandService.createRequisition(memberId, request);
        return ResponseEntity.ok(requisitionId);
    }


    @Operation(summary = "품의서 상신", description = "로그인한 사용자가 작성한 임시 저장 품의서를 상신한다.")
    @ApiResponse(responseCode = "200", description = "상신 완료")
    @PatchMapping("/{requisitionId}/submit")
    public ResponseEntity<Void> submitRequisition(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @PathVariable @NotNull Long requisitionId
    ) {
        Long memberId = userDetail.getMemberId(); // 추후 구현될 CustomUserDetails에 맞게
        requisitionCommandService.submitRequisition(requisitionId, memberId);
        return ResponseEntity.ok().build();
    }
}
*/

    @PostMapping("/create/test")
    public ResponseEntity<Long> createRequisitionTest(
            @RequestParam(name = "memberId", defaultValue = "1") Long memberId,
            @Valid @RequestBody CreateRequisitionRequest request
    ) {
        Long id = requisitionCommandService.createRequisition(memberId, request);
        return ResponseEntity.ok(id);
    }

    @PatchMapping("/{id}/submit/test")
    public ResponseEntity<Void> submitRequisition(
            @PathVariable Long id,
            @RequestParam(name = "memberId", defaultValue = "1") Long memberId
    ) {
        requisitionCommandService.submitRequisition(memberId, id);
        return ResponseEntity.ok().build();
    }
}

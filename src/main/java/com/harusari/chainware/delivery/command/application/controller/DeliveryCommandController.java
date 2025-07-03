package com.harusari.chainware.delivery.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;
import com.harusari.chainware.delivery.command.application.service.DeliveryCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
@Tag(name = "배송 Command API", description = "배송 시작, 완료 API")
public class DeliveryCommandController {

    private final DeliveryCommandService deliveryCommandService;

    @PutMapping("/{deliveryId}/start")
    @Operation(summary = "배송 시작", description = "창고 관리자가 배송을 시작합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "배송 시작됨")
    })
    public ResponseEntity<ApiResponse<DeliveryCommandResponse>> startDelivery(
            @PathVariable Long deliveryId,
            @RequestBody DeliveryStartRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        DeliveryCommandResponse response = deliveryCommandService.startDelivery(deliveryId, request, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{deliveryId}/complete")
    @Operation(summary = "배송 완료", description = "가맹점 관리자가 배송을 완료합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "배송 완료됨")
    })
    public ResponseEntity<ApiResponse<DeliveryCommandResponse>> completeDelivery(
            @PathVariable Long deliveryId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        DeliveryCommandResponse response = deliveryCommandService.completeDelivery(deliveryId, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

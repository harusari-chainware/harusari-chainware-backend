package com.harusari.chainware.delivery.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;
import com.harusari.chainware.delivery.command.application.service.DeliveryCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/delivery")
@RequiredArgsConstructor
public class DeliveryCommandController {

    private final DeliveryCommandService deliveryCommandService;

    // 배송 시작
    @PutMapping("/{deliveryId}/start")
    public ResponseEntity<ApiResponse<DeliveryCommandResponse>> startDelivery(
            @PathVariable Long deliveryId,
            @RequestBody DeliveryStartRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        DeliveryCommandResponse response = deliveryCommandService.startDelivery(deliveryId, request, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 배송 완료
    @PutMapping("/{deliveryId}/complete")
    public ResponseEntity<ApiResponse<DeliveryCommandResponse>> completeDelivery(
            @PathVariable Long deliveryId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        DeliveryCommandResponse response = deliveryCommandService.completeDelivery(deliveryId, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

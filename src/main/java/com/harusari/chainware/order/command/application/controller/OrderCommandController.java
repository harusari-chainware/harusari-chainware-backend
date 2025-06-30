package com.harusari.chainware.order.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.order.command.application.dto.request.OrderApproveRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderRejectRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderUpdateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.application.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    // 주문 등록
    @PostMapping
    public ResponseEntity<ApiResponse<OrderCommandResponse>> createOrder(
            @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.createOrder(request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 수정
    @PutMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.updateOrder(orderId, request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 취소
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.cancelOrder(orderId, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 승인
    @PutMapping("/{orderId}/approve")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> approveOrder(
            @PathVariable Long orderId,
            @RequestBody OrderApproveRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.approveOrder(orderId, request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 반려
    @PutMapping("/{orderId}/reject")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> rejectOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRejectRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.rejectOrder(orderId, request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

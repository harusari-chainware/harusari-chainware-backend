package com.harusari.chainware.order.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderRejectRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderUpdateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.application.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    // 주문 등록
    @PostMapping("/orders")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> createOrder(
            @RequestBody OrderCreateRequest request
    ) {
        System.out.println("createOrder 메소드 실행");
        OrderCommandResponse response = orderCommandService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 수정
    @PutMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest request
    ) {
        OrderCommandResponse response = orderCommandService.updateOrder(orderId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 취소
    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> cancelOrder(
            @PathVariable Long orderId
    ) {
        OrderCommandResponse response = orderCommandService.cancelOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 승인
    @PutMapping("/orders/{orderId}/approve")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> approveOrder(
            @PathVariable Long orderId
    ) {
        OrderCommandResponse response = orderCommandService.approveOrder(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 주문 반려
    @PutMapping("/orders/{orderId}/reject")
    public ResponseEntity<ApiResponse<OrderCommandResponse>> rejectOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRejectRequest request
    ) {
        OrderCommandResponse response = orderCommandService.rejectOrder(orderId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

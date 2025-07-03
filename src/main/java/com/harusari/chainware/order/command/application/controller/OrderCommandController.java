package com.harusari.chainware.order.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.order.command.application.dto.request.OrderApproveRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderRejectRequest;
import com.harusari.chainware.order.command.application.dto.request.OrderUpdateRequest;
import com.harusari.chainware.order.command.application.dto.response.OrderCommandResponse;
import com.harusari.chainware.order.command.application.service.OrderCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "주문 Command API", description = "주문 등록, 수정, 취소, 승인, 반려 API")
public class OrderCommandController {

    private final OrderCommandService orderCommandService;

    @PostMapping
    @Operation(summary = "주문 등록", description = "가맹점 관리자가 주문을 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 등록됨")
    })
    public ResponseEntity<ApiResponse<OrderCommandResponse>> createOrder(
            @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.createOrder(request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "주문 수정", description = "가맹점 관리자가 주문을 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 수정됨")
    })
    public ResponseEntity<ApiResponse<OrderCommandResponse>> updateOrder(
            @PathVariable Long orderId,
            @RequestBody OrderUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.updateOrder(orderId, request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "주문 취소", description = "가맹점 관리자가 본인이 등록한 주문을 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 취소됨")
    })
    public ResponseEntity<ApiResponse<OrderCommandResponse>> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.cancelOrder(orderId, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{orderId}/approve")
    @Operation(summary = "주문 승인", description = "본사 관리자가 주문을 승인합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 승인됨")
    })
    public ResponseEntity<ApiResponse<OrderCommandResponse>> approveOrder(
            @PathVariable Long orderId,
            @RequestBody OrderApproveRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.approveOrder(orderId, request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{orderId}/reject")
    @Operation(summary = "주문 반려", description = "본사 관리자가 주문을 반려합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 반려됨")
    })
    public ResponseEntity<ApiResponse<OrderCommandResponse>> rejectOrder(
            @PathVariable Long orderId,
            @RequestBody OrderRejectRequest request,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        OrderCommandResponse response = orderCommandService.rejectOrder(orderId, request, customUserDetails.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

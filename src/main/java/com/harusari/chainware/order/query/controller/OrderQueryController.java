package com.harusari.chainware.order.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.order.query.dto.request.OrderSearchRequest;
import com.harusari.chainware.order.query.dto.response.OrderSearchDetailResponse;
import com.harusari.chainware.order.query.dto.response.OrderSearchResponse;
import com.harusari.chainware.order.query.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderQueryController {

    private final OrderQueryService orderQueryService;

    // 해당 주문에 대해 가능한 창고 목록 조회
    @GetMapping("/{orderId}/available-warehouses")
    public ResponseEntity<ApiResponse<PageResponse<OrderSearchResponse>>> availableWarehouse(
            @PathVariable Long orderId,
            @ModelAttribute OrderSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PageResponse<OrderSearchResponse> response = orderQueryService.searchOrders(request, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    // 주문 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<OrderSearchResponse>>> searchOrders(
            @ModelAttribute OrderSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PageResponse<OrderSearchResponse> response = orderQueryService.searchOrders(request, pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    // 주문 상세 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderSearchDetailResponse>> getOrderDetail(
            @PathVariable Long orderId
    ) {
        OrderSearchDetailResponse response = orderQueryService.getOrderDetail(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

}

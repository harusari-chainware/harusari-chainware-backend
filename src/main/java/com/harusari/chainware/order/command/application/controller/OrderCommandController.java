package com.harusari.chainware.order.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.order.command.application.dto.request.OrderCreateRequest;
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
    public ResponseEntity<ApiResponse<OrderCommandResponse>> createOrder(@RequestBody OrderCreateRequest request) {
        OrderCommandResponse response = orderCommandService.createOrder(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

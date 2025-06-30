package com.harusari.chainware.order.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.order.query.dto.response.AvailableWarehouseResponse;
import com.harusari.chainware.order.query.service.OrderWarehouseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderWarehouseQueryController {

    private final OrderWarehouseQueryService orderWarehouseQueryService;

    // 주문 가능 창고 조회
    @GetMapping("/{orderId}/available-warehouses")
    public ResponseEntity<ApiResponse<List<AvailableWarehouseResponse>>> getAvailableWarehouses(
            @PathVariable Long orderId
    ) {
        List<AvailableWarehouseResponse> response = orderWarehouseQueryService.findAvailableWarehouses(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

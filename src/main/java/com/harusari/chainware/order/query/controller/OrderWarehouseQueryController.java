package com.harusari.chainware.order.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.order.query.dto.response.AvailableWarehouseResponse;
import com.harusari.chainware.order.query.service.OrderWarehouseQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "주문에 대한 창고 Query API", description = "주문 승인 가능 창고 조회 API")
public class OrderWarehouseQueryController {

    private final OrderWarehouseQueryService orderWarehouseQueryService;

    @GetMapping("/{orderId}/available-warehouses")
    @Operation(summary = "주문 승인 가능 창고 목록 조회", description = "주문 ID를 기준으로 주문이 승인 가능한 창고의 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "주문 가능 창고  성공")
    })
    public ResponseEntity<ApiResponse<List<AvailableWarehouseResponse>>> getAvailableWarehouses(
            @PathVariable Long orderId
    ) {
        List<AvailableWarehouseResponse> response = orderWarehouseQueryService.findAvailableWarehouses(orderId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

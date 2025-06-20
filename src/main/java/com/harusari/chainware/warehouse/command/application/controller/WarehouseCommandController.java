package com.harusari.chainware.warehouse.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;
import com.harusari.chainware.warehouse.command.application.service.WarehouseCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseCommandController {

    private final WarehouseCommandService warehouseCommandService;

    // 창고마스터 수정
    @PutMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<WarehouseCommandResponse>> updateWarehouse(
            @PathVariable Long warehouseId,
            @RequestBody WarehouseUpdateRequest request
    ) {
        WarehouseCommandResponse response = warehouseCommandService.updateWarehouse(warehouseId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 창고마스터 삭제
    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<WarehouseCommandResponse>> deleteWarehouse(@PathVariable Long warehouseId) {
        WarehouseCommandResponse response = warehouseCommandService.deleteWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 보유재고 등록

    // 보유재고 수정

    // 보유재고 삭제

    // 입고 정보 등록

}

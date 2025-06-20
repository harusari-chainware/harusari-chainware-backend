package com.harusari.chainware.warehouse.command.application.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventoryCommandResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryCreateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryUpdateRequest;
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

    // 보유 재고 등록
    @PostMapping("/{warehouseId}/inventory")
    public ResponseEntity<ApiResponse<WarehouseCommandResponse>> registerInventory(
            @PathVariable Long warehouseId,
            @RequestBody WarehouseInventoryCreateRequest request
    ) {
        WarehouseCommandResponse response = warehouseCommandService.registerInventory(warehouseId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 보유 재고 수정
    @PutMapping("/inventory/{inventoryId}")
    public ResponseEntity<ApiResponse<WarehouseInventoryCommandResponse>> updateInventory(
            @PathVariable Long inventoryId,
            @RequestBody WarehouseInventoryUpdateRequest request
    ) {
        WarehouseInventoryCommandResponse response = warehouseCommandService.updateInventory(inventoryId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 보유 재고 삭제
    @DeleteMapping("/inventory/{inventoryId}")
    public ResponseEntity<ApiResponse<WarehouseInventoryCommandResponse>> deleteInventory(@PathVariable Long inventoryId) {
        WarehouseInventoryCommandResponse response = warehouseCommandService.deleteInventory(inventoryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

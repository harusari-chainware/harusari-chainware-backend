package com.harusari.chainware.warehouse.command.application.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventoryCommandResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryCreateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;
import com.harusari.chainware.warehouse.command.application.service.WarehouseCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Tag(name = "창고 Command API", description = "창고 수정, 삭제 및 보유 재고 등록, 수정, 삭제 API")
public class WarehouseCommandController {

    private final WarehouseCommandService warehouseCommandService;

    @PutMapping("/{warehouseId}")
    @Operation(summary = "창고 마스터 수정", description = "마스터가 창고 마스터 ID를 기준으로 창고 정보를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "창고 마스터 수정됨")
    })
    public ResponseEntity<ApiResponse<WarehouseCommandResponse>> updateWarehouse(
            @PathVariable Long warehouseId,
            @RequestBody WarehouseUpdateRequest request
    ) {
        WarehouseCommandResponse response = warehouseCommandService.updateWarehouse(warehouseId, request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{warehouseId}")
    @Operation(summary = "창고 마스터 삭제", description = "마스터가 창고 마스터 ID를 기준으로 창고 정보를 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "창고 마스터 삭제됨")
    })
    public ResponseEntity<ApiResponse<WarehouseCommandResponse>> deleteWarehouse(
            @PathVariable Long warehouseId
    ) {
        WarehouseCommandResponse response = warehouseCommandService.deleteWarehouse(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{warehouseId}/inventory")
    @Operation(summary = "보유 재고 등록", description = "창고 관리자가 창고 마스터 ID를 기준으로 보유 재고를 등록합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "보유 재고 등록됨")
    })
    public ResponseEntity<ApiResponse<WarehouseCommandResponse>> registerInventory(
            @PathVariable Long warehouseId,
            @RequestBody WarehouseInventoryCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        WarehouseCommandResponse response = warehouseCommandService.registerInventory(warehouseId, request, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/inventory/{inventoryId}")
    @Operation(summary = "보유 재고 수정", description = "창고 관리자가 창고 마스터 ID를 기준으로 보유 재고를 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "보유 재고 수정됨")
    })
    public ResponseEntity<ApiResponse<WarehouseInventoryCommandResponse>> updateInventory(
            @PathVariable Long inventoryId,
            @RequestBody WarehouseInventoryUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        WarehouseInventoryCommandResponse response = warehouseCommandService.updateInventory(inventoryId, request, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/inventory/{inventoryId}")
    @Operation(summary = "보유 재고 삭제", description = "창고 관리자가 창고 마스터 ID를 기준으로 보유 재고를 삭제합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "보유 재고 삭제됨")
    })
    public ResponseEntity<ApiResponse<WarehouseInventoryCommandResponse>> deleteInventory(
            @PathVariable Long inventoryId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        WarehouseInventoryCommandResponse response = warehouseCommandService.deleteInventory(inventoryId, user.getMemberId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

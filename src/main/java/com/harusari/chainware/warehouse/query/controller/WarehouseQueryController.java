package com.harusari.chainware.warehouse.query.controller;

import com.harusari.chainware.auth.model.CustomUserDetails;
import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseInventorySearchRequest;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.*;
import com.harusari.chainware.warehouse.query.service.WarehouseQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
@Tag(name = "창고 Query API", description = "창고 및 보유재고의 목록, 상세 조회 API")
public class WarehouseQueryController {

    private final WarehouseQueryService warehouseQueryService;

    @GetMapping
    @Operation(summary = "창고 마스터 목록 조회", description = "창고 마스터 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "창고 마스터 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<PageResponse<WarehouseSearchResponse>>> searchWarehouses(
            @ModelAttribute WarehouseSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PageResponse<WarehouseSearchResponse> response = warehouseQueryService.searchWarehouses(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    @GetMapping("/{warehouseId}")
    @Operation(summary = "창고 마스터 상세 조회", description = "창고 마스터 ID를 기준으로 창고를 상세 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "창고 마스터 상세 조회 성공")
    })
    public ResponseEntity<ApiResponse<WarehouseDetailResponse>> findWarehouseDetail(
            @PathVariable Long warehouseId
    ) {
        WarehouseDetailResponse response = warehouseQueryService.findWarehouseDetailById(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/inventory")
    @Operation(summary = "보유 재고 목록 조회", description = "보유 재고 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "보유 재고 목록 조회 성공")
    })
    public ResponseEntity<ApiResponse<PageResponse<WarehouseInventoryInfo>>> getWarehouseInventories(
            @ModelAttribute WarehouseInventorySearchRequest request,
            @PageableDefault(size=10) Pageable pageable
    ) {
        PageResponse<WarehouseInventoryInfo> response = warehouseQueryService.getWarehouseInventories(request, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/inventory/{inventoryId}")
    @Operation(summary = "보유 재고 상세 조회", description = "보유 재고 ID를 기준으로 보유 재고를 상세 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "보유 재고 상세 조회 성공")
    })
    public ResponseEntity<ApiResponse<WarehouseInventoryDetailResponse>> getWarehouseInventoryDetail(
            @PathVariable Long inventoryId
    ) {
        WarehouseInventoryDetailResponse response = warehouseQueryService.getWarehouseInventoryDetail(inventoryId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "전체 창고 목록 조회")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<WarehouseSimpleResponse>>> getAllWarehouses() {
        List<WarehouseSimpleResponse> list = warehouseQueryService.getAllWarehouses();
        return ResponseEntity.ok(ApiResponse.success(list));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Long>> getMyWarehouseId(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getMemberId();
        Long warehouseId = warehouseQueryService.getWarehouseIdByManagerId(memberId);
        return ResponseEntity.ok(ApiResponse.success(warehouseId));
    }
}

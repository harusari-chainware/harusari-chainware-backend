package com.harusari.chainware.warehouse.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseDetailResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSearchResponse;
import com.harusari.chainware.warehouse.query.service.WarehouseQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/warehouse")
@RequiredArgsConstructor
public class WarehouseQueryController {

    private final WarehouseQueryService warehouseQueryService;

    // 창고 마스터 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WarehouseSearchResponse>>> searchWarehouses(
            @ModelAttribute WarehouseSearchRequest request,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        PageResponse<WarehouseSearchResponse> response = warehouseQueryService.searchWarehouses(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response));
    }

    // 창고 마스터 상세 조회
    @GetMapping("/{warehouseId}")
    public ResponseEntity<ApiResponse<WarehouseDetailResponse>> findWarehouseDetail(
            @PathVariable Long warehouseId
    ) {
        WarehouseDetailResponse response = warehouseQueryService.findWarehouseDetailById(warehouseId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

}

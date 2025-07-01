package com.harusari.chainware.warehouse.query.controller;

import com.harusari.chainware.common.dto.ApiResponse;
import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSearchResponse;
import com.harusari.chainware.warehouse.query.service.WarehouseQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

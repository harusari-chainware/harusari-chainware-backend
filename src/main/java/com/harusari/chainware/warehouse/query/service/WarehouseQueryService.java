package com.harusari.chainware.warehouse.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseInventorySearchRequest;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseDetailResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseInventoryDetailResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseInventoryInfo;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSearchResponse;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WarehouseQueryService {
    PageResponse<WarehouseSearchResponse> searchWarehouses(WarehouseSearchRequest request, Pageable pageable);
    WarehouseDetailResponse findWarehouseDetailById(Long warehouseId);
    PageResponse<WarehouseInventoryInfo> getWarehouseInventories(WarehouseInventorySearchRequest request, Pageable pageable);
    WarehouseInventoryDetailResponse getWarehouseInventoryDetail(Long warehouseInventoryId);
    Long getWarehouseIdByManagerId(Long memberId);
}

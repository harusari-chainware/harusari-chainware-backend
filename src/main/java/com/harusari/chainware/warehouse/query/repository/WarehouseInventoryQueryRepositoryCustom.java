package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.warehouse.query.dto.request.WarehouseInventorySearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseInventoryDetailResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseInventoryInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseInventoryQueryRepositoryCustom {
    Page<WarehouseInventoryInfo> getWarehouseInventories(WarehouseInventorySearchRequest request, Pageable pageable);
    WarehouseInventoryDetailResponse findWarehouseInventoryDetail(Long warehouseInventoryId);
}

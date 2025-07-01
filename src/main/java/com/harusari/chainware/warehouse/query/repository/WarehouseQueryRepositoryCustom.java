package com.harusari.chainware.warehouse.query.repository;

import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WarehouseQueryRepositoryCustom {
    Page<WarehouseSearchResponse> searchWarehouses(WarehouseSearchRequest request, Pageable pageable);
}

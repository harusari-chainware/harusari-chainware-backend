package com.harusari.chainware.warehouse.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseInventorySearchRequest;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseDetailResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseInventoryDetailResponse;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseInventoryInfo;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSearchResponse;
import com.harusari.chainware.warehouse.query.repository.WarehouseInventoryQueryRepository;
import com.harusari.chainware.warehouse.query.repository.WarehouseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseQueryServiceImpl implements WarehouseQueryService{

    private final WarehouseQueryRepository warehouseQueryRepository;
    private final WarehouseInventoryQueryRepository warehouseInventoryQueryRepository;

    // 창고 마스터 목록 조회
    @Override
    public PageResponse<WarehouseSearchResponse> searchWarehouses(WarehouseSearchRequest request, Pageable pageable) {
        return PageResponse.from(warehouseQueryRepository.searchWarehouses(request, pageable));
    }

    // 창고 마스터 상세 조회
    @Override
    public WarehouseDetailResponse findWarehouseDetailById(Long warehouseId) {
        return warehouseQueryRepository.findWarehouseDetailById(warehouseId);
    }

    // 보유 재고 목록 조회
    @Override
    public PageResponse<WarehouseInventoryInfo> getWarehouseInventories(WarehouseInventorySearchRequest request, Pageable pageable) {
        return PageResponse.from(warehouseInventoryQueryRepository.getWarehouseInventories(request, pageable));
    }

    // 보유 재고 상세 조회
    @Override
    public WarehouseInventoryDetailResponse getWarehouseInventoryDetail(Long warehouseInventoryId) {
        return warehouseInventoryQueryRepository.findWarehouseInventoryDetail(warehouseInventoryId);
    }

    @Override
    public Long getWarehouseIdByManagerId(Long memberId) {
        return warehouseQueryRepository.findWarehouseIdByManagerId(memberId);
    }


}

package com.harusari.chainware.warehouse.query.service;

import com.harusari.chainware.common.dto.PageResponse;
import com.harusari.chainware.warehouse.query.dto.request.WarehouseSearchRequest;
import com.harusari.chainware.warehouse.query.dto.response.WarehouseSearchResponse;
import com.harusari.chainware.warehouse.query.repository.WarehouseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseQueryServiceImpl implements WarehouseQueryService{

    private final WarehouseQueryRepository warehouseQueryRepository;

    // 창고 마스터 목록 조회
    @Override
    public PageResponse<WarehouseSearchResponse> searchWarehouses(WarehouseSearchRequest request, Pageable pageable) {
        return PageResponse.from(warehouseQueryRepository.searchWarehouses(request, pageable));
    }

}

package com.harusari.chainware.warehouse.command.application.service;

import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.infrastructure.repository.JpaWarehouseRepository;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseCommandServiceImpl implements WarehouseCommandService{

    private final JpaWarehouseRepository jpaWarehouseRepository;

    // 창고 마스터 수정
    @Override
    public WarehouseCommandResponse updateWarehouse(Long warehouseId, WarehouseUpdateRequest request) {
        // 1. 창고 조회
        Warehouse warehouse = jpaWarehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        // 2. 창고 정보 수정
        warehouse.updateInfo(
                request.getWarehouseName(),
                request.getWarehouseAddress(),
                request.isWarehouseStatus(),
                LocalDateTime.now()
        );

        return toResponse(warehouse);
    }

    // 창고 마스터 삭제
    @Override
    public WarehouseCommandResponse deleteWarehouse(Long warehouseId) {
        // 1. 창고 조회
        Warehouse warehouse = jpaWarehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        // 2. 창고 상태 검증
        if (warehouse.isWarehouseStatus()) {
            throw new WarehouseException(WarehouseErrorCode.INVALID_WAREHOUSE_STATUS);
        }

        // 3. 창고 삭제
        warehouse.softDelete();

        return toResponse(warehouse);
    }

    private WarehouseCommandResponse toResponse(Warehouse warehouse) {
        return WarehouseCommandResponse.builder()
                .warehouseId(warehouse.getWarehouseId())
                .warehouseName(warehouse.getWarehouseName())
                .warehouseAddress(warehouse.getWarehouseAddress())
                .warehouseStatus(warehouse.isWarehouseStatus())
                .build();
    }

}

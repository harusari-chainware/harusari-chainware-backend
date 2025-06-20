package com.harusari.chainware.warehouse.command.application.service;

import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventoryCommandResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryCreateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.infrastructure.repository.JpaWarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.infrastructure.repository.JpaWarehouseRepository;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseCommandServiceImpl implements WarehouseCommandService{

    private final JpaWarehouseRepository jpaWarehouseRepository;
    private final JpaWarehouseInventoryRepository jpaWarehouseInventoryRepository;

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

        return toWarehouseResponse(warehouse);
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

        return toWarehouseResponse(warehouse);
    }

    // 보유 재고 등록
    @Override
    public WarehouseCommandResponse registerInventory(Long warehouseId, WarehouseInventoryCreateRequest request) {
        // 1. 창고 검증
        Warehouse warehouse = jpaWarehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        // 2. 요청한 보유 재고 확인
        List<WarehouseInventory> inventories = request.getItems().stream().map(item ->
                WarehouseInventory.builder()
                        .warehouseId(warehouseId)
                        .productId(item.getProductId())
                        .contractId(item.getContractId())
                        .quantity(item.getQuantity())
                        .reservedQuantity(0)
                        .createdAt(LocalDateTime.now())
                        .build()
        ).toList();

        // 3. 보유 재고 등록
        jpaWarehouseInventoryRepository.saveAll(inventories);

        return toWarehouseResponse(warehouse);
    }

    // 보유 재고 수정
    @Override
    public WarehouseInventoryCommandResponse updateInventory(Long inventoryId, WarehouseInventoryUpdateRequest request) {
        // 1. 창고 검증
        WarehouseInventory inventory = jpaWarehouseInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND));

        // 2. 재고 수량 수정
        inventory.updateQuantity(request.getQuantity(), LocalDateTime.now());

        return toInventoryResponse(inventory);
    }

    // 보유 재고 삭제
    @Override
    public WarehouseInventoryCommandResponse deleteInventory(Long inventoryId) {
        // 1. 창고 검증
        WarehouseInventory inventory = jpaWarehouseInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND));

        // 2. 재고 삭제
        jpaWarehouseInventoryRepository.delete(inventory);

        return toInventoryResponse(inventory);
    }

    private WarehouseCommandResponse toWarehouseResponse(Warehouse warehouse) {
        return WarehouseCommandResponse.builder()
                .warehouseId(warehouse.getWarehouseId())
                .warehouseName(warehouse.getWarehouseName())
                .warehouseAddress(warehouse.getWarehouseAddress())
                .warehouseStatus(warehouse.isWarehouseStatus())
                .build();
    }

    private WarehouseInventoryCommandResponse toInventoryResponse(WarehouseInventory inventory) {
        return WarehouseInventoryCommandResponse.builder()
                .inventoryId(inventory.getWarehouseInventoryId())
                .productId(inventory.getProductId())
                .contractId(inventory.getContractId())
                .quantity(inventory.getQuantity())
                .build();
    }

}

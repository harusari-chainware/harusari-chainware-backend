package com.harusari.chainware.warehouse.command.application.service;

import com.harusari.chainware.common.domain.vo.Address;
import com.harusari.chainware.common.mapstruct.AddressMapStruct;
import com.harusari.chainware.product.command.infrastructure.JpaProductRepository;
import com.harusari.chainware.product.command.domain.aggregate.Product;
import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventoryCommandResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryCreateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;
import com.harusari.chainware.warehouse.command.domain.aggregate.Warehouse;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventory;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventoryRepository;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseRepository;
import com.harusari.chainware.warehouse.exception.WarehouseErrorCode;
import com.harusari.chainware.warehouse.exception.WarehouseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class WarehouseCommandServiceImpl implements WarehouseCommandService{

    private final AddressMapStruct addressMapStruct;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final JpaProductRepository productRepository;

    // 창고 마스터 수정
    @Override
    public WarehouseCommandResponse updateWarehouse(Long warehouseId, WarehouseUpdateRequest request) {
        // 1. 창고 조회
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        Address address = addressMapStruct.toAddress(request.getWarehouseAddress());

        // 2. 창고 정보 수정
        warehouse.updateInfo(
                request.getWarehouseName(),
                address,
                request.isWarehouseStatus(),
                LocalDateTime.now()
        );

        return toWarehouseResponse(warehouse);
    }

    // 창고 마스터 삭제
    @Override
    public WarehouseCommandResponse deleteWarehouse(Long warehouseId) {
        // 1. 창고 조회
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
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
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.WAREHOUSE_NOT_FOUND));

        // 2. 등록하려는 보유 재고 제품 ID 추출
        List<Long> productIds = request.getItems().stream()
                .map(WarehouseInventoryCreateRequest.InventoryItem::getProductId)
                .toList();

        // 3. 이미 등록된 재고 확인 → 중복 방지
        List<WarehouseInventory> alreadyRegistered = warehouseInventoryRepository.findByWarehouseIdAndProductIdIn(warehouseId, productIds);
        if (!alreadyRegistered.isEmpty()) {
            List<Long> duplicateProductIds = alreadyRegistered.stream()
                    .map(WarehouseInventory::getProductId)
                    .toList();
            throw new WarehouseException(WarehouseErrorCode.DUPLICATE_INVENTORY_ALREADY_EXISTS);
        }

        // 3. 제품의 안전재고 추출
        Map<Long, Integer> safetyQuantityMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getProductId, Product::getSafetyStock));

        // 4. 보유 재고 엔티티 구성
        List<WarehouseInventory> inventories = request.getItems().stream()
                .map(item -> {
                    Integer safetyQuantity = safetyQuantityMap.get(item.getProductId());
                    if (safetyQuantity == null) {
                        throw new WarehouseException(WarehouseErrorCode.PRODUCT_NOT_FOUND_FOR_INVENTORY);
                    }

                    return WarehouseInventory.builder()
                            .warehouseId(warehouseId)
                            .productId(item.getProductId())
                            .quantity(item.getQuantity())
                            .reservedQuantity(0)
                            .safetyQuantity(safetyQuantity)
                            .build();
                })
                .toList();

        // 5. 보유 재고 등록
        warehouseInventoryRepository.saveAll(inventories);

        return toWarehouseResponse(warehouse);
    }

    // 보유 재고 수정
    @Override
    public WarehouseInventoryCommandResponse updateInventory(Long inventoryId, WarehouseInventoryUpdateRequest request) {
        // 1. 창고 검증
        WarehouseInventory inventory = warehouseInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND));

        // 2. 재고 수량 수정
        inventory.updateQuantity(request.getQuantity(), LocalDateTime.now());

        return toInventoryResponse(inventory);
    }

    // 보유 재고 삭제
    @Override
    public WarehouseInventoryCommandResponse deleteInventory(Long inventoryId) {
        // 1. 창고 검증
        WarehouseInventory inventory = warehouseInventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new WarehouseException(WarehouseErrorCode.INVENTORY_NOT_FOUND));

        // 2. 재고 삭제
        warehouseInventoryRepository.delete(inventory);

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
                .quantity(inventory.getQuantity())
                .build();
    }

}
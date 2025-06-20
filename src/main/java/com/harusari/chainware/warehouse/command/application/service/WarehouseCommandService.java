package com.harusari.chainware.warehouse.command.application.service;

import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventoryCommandResponse;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryCreateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseInventoryUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;

public interface WarehouseCommandService {

    WarehouseCommandResponse updateWarehouse(Long warehouseId, WarehouseUpdateRequest request);
    WarehouseCommandResponse deleteWarehouse(Long warehouseId);
    WarehouseCommandResponse registerInventory(Long warehouseId, WarehouseInventoryCreateRequest request);
    WarehouseInventoryCommandResponse updateInventory(Long inventoryId, WarehouseInventoryUpdateRequest request);
    WarehouseInventoryCommandResponse deleteInventory(Long inventoryId);


}

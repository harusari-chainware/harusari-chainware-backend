package com.harusari.chainware.warehouse.command.application.service;

import com.harusari.chainware.warehouse.command.application.dto.request.WarehouseUpdateRequest;
import com.harusari.chainware.warehouse.command.application.dto.response.WarehouseCommandResponse;

public interface WarehouseCommandService {

    WarehouseCommandResponse updateWarehouse(Long warehouseId, WarehouseUpdateRequest request);
    WarehouseCommandResponse deleteWarehouse(Long warehouseId);

}

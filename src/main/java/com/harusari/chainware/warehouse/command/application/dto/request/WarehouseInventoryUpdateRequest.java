package com.harusari.chainware.warehouse.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseInventoryUpdateRequest {
    private int quantity;
}
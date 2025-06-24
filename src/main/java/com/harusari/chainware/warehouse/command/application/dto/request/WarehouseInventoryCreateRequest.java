package com.harusari.chainware.warehouse.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseInventoryCreateRequest {

    private List<InventoryItem> items;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class InventoryItem {
        private Long productId;
        private Long contractId;
        private int quantity;
    }
}

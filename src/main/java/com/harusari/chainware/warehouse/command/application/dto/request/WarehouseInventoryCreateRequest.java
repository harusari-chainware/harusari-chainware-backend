package com.harusari.chainware.warehouse.command.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class WarehouseInventoryCreateRequest {

    private List<InventoryItem> items;

    @Getter
    public static class InventoryItem {
        private Long productId;
        private Long contractId;
        private int quantity;
    }
}

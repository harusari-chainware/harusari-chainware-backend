package com.harusari.chainware.warehouse.command.application.controller;

import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventorySnapshotResponseDto;
import com.harusari.chainware.warehouse.command.application.service.InventorySnapshotScheduler;
import com.harusari.chainware.warehouse.command.mapper.WarehouseInventorySnapshotQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/inventory-snapshot")
@RequiredArgsConstructor
public class WarehouseInventorySnapshotTestController {

    private final InventorySnapshotScheduler inventorySnapshotScheduler;
    private final WarehouseInventorySnapshotQueryMapper snapshotQueryMapper;

    @PostMapping("/manual")
    public void triggerSnapshot(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate snapshotDate
    ) {
        inventorySnapshotScheduler.saveDailySnapshot(snapshotDate);
    }

    @GetMapping
    public List<WarehouseInventorySnapshotResponseDto> getSnapshot(
            @RequestParam(required = false) Long warehouseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return snapshotQueryMapper.selectSnapshotByDate(warehouseId, targetDate);
    }
}
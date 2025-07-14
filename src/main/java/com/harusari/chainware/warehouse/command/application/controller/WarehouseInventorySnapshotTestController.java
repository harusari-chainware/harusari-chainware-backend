package com.harusari.chainware.warehouse.command.application.controller;

import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventorySnapshotResponseDto;
import com.harusari.chainware.warehouse.command.application.service.InventorySnapshotScheduler;
import com.harusari.chainware.warehouse.command.mapper.WarehouseInventorySnapshotQueryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/statistics/inventory-snapshot")
@RequiredArgsConstructor
@Tag(name = "재고 스냅샷 통계 (테스트용)", description = "창고 재고 스냅샷 수동 저장 및 조회 API입니다.")
public class WarehouseInventorySnapshotTestController {

    private final InventorySnapshotScheduler inventorySnapshotScheduler;
    private final WarehouseInventorySnapshotQueryMapper snapshotQueryMapper;

    @Operation(
            summary = "재고 스냅샷 수동 저장",
            description = "특정 날짜에 대한 재고 스냅샷을 수동으로 저장합니다. 날짜를 지정하지 않으면 오늘 날짜 기준으로 저장됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스냅샷 저장 성공")
            }
    )
    @PostMapping("/manual")
    public void triggerSnapshot(
            @Parameter(description = "스냅샷 저장 대상 날짜 (yyyy-MM-dd), 생략 시 오늘로 처리됨", example = "2025-07-14")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate snapshotDate
    ) {
        inventorySnapshotScheduler.saveDailySnapshot(snapshotDate);
    }

    @Operation(
            summary = "재고 스냅샷 조회",
            description = "특정 날짜의 재고 스냅샷을 조회합니다. 창고 ID를 지정하지 않으면 전체 창고 기준으로 조회됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "스냅샷 조회 성공")
            }
    )
    @GetMapping
    public List<WarehouseInventorySnapshotResponseDto> getSnapshot(
            @Parameter(description = "조회할 창고 ID (선택)", example = "1")
            @RequestParam(required = false) Long warehouseId,

            @Parameter(description = "조회할 날짜 (yyyy-MM-dd)", required = true, example = "2025-07-13")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate targetDate
    ) {
        return snapshotQueryMapper.selectSnapshotByDate(warehouseId, targetDate);
    }
}

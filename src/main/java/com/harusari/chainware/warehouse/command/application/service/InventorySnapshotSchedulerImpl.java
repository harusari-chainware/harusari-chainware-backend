package com.harusari.chainware.warehouse.command.application.service;

import com.harusari.chainware.warehouse.command.application.dto.CurrentWarehouseInventoryDto;
import com.harusari.chainware.warehouse.command.domain.aggregate.WarehouseInventorySnapshot;
import com.harusari.chainware.warehouse.command.domain.repository.WarehouseInventorySnapshotRepository;
import com.harusari.chainware.warehouse.command.infrastructure.repository.JpaWarehouseInventorySnapshotRepository;
import com.harusari.chainware.warehouse.common.mapper.WarehouseInventorySnapshotQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventorySnapshotSchedulerImpl implements InventorySnapshotScheduler {

    private final WarehouseInventorySnapshotQueryMapper warehouseInventoryQueryMapper;
    private final WarehouseInventorySnapshotRepository warehouseInventorySnapshotRepository;
    private final JpaWarehouseInventorySnapshotRepository jpaRepository;

    @Scheduled(cron = "0 1 0 * * *")
    @Override
    @Transactional
    public void saveDailySnapshot(LocalDate snapshotDate) {
        LocalDate snapshotTargetDate = (snapshotDate != null) ? snapshotDate : LocalDate.now().minusDays(1);
        LocalDateTime recordedAt = LocalDateTime.now();

        List<CurrentWarehouseInventoryDto> currentInventories = warehouseInventoryQueryMapper.findAllCurrentInventory();

        for (CurrentWarehouseInventoryDto inventory : currentInventories) {
            boolean exists = warehouseInventorySnapshotRepository.existsByWarehouseIdAndProductIdAndSnapshotDate(
                    inventory.getWarehouseId(), inventory.getProductId(), snapshotTargetDate);

            if (!exists) {
                WarehouseInventorySnapshot snapshot = WarehouseInventorySnapshot.builder()
                        .warehouseId(inventory.getWarehouseId())
                        .productId(inventory.getProductId())
                        .snapshotDate(snapshotTargetDate)
                        .quantity(inventory.getQuantity())
                        .recordedAt(recordedAt)
                        .build();

                jpaRepository.save(snapshot);
            }
        }

        log.info("✅ Warehouse inventory snapshot saved for {}", snapshotTargetDate);
    }
}

package com.harusari.chainware.warehouse.command.mapper;

import com.harusari.chainware.warehouse.command.application.dto.CurrentWarehouseInventoryDto;
import com.harusari.chainware.warehouse.command.application.dto.WarehouseInventorySnapshotResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface WarehouseInventorySnapshotQueryMapper {
    List<CurrentWarehouseInventoryDto> findAllCurrentInventory();

    List<WarehouseInventorySnapshotResponseDto> selectSnapshotByDate(
            @Param("warehouseId") Long warehouseId,
            @Param("snapshotDate") LocalDate snapshotDate
    );
}

package com.harusari.chainware.statistics.query.dto.inventoryTurnover;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class InventoryTurnoverTrendResponse {
    private LocalDate date;
    private Double turnoverRate;
}
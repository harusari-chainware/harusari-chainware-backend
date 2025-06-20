package com.harusari.chainware.warehouse.command.application.service;

import java.time.LocalDate;

public interface InventorySnapshotScheduler {
    void saveDailySnapshot(LocalDate snapshotDate);
}
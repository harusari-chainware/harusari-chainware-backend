package com.harusari.chainware.takeback.query.dto.request;

import com.harusari.chainware.takeback.command.domain.aggregate.TakeBackStatus;

import java.time.LocalDate;

public record TakeBackSearchRequest(
        Long warehouseId,
        String warehouseName,
        String warehouseAddress,
        String franchiseName,
        LocalDate fromDate,
        LocalDate toDate,
        TakeBackStatus takeBackStatus
) {}

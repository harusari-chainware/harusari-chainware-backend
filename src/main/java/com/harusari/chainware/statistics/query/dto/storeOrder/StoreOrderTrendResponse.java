package com.harusari.chainware.statistics.query.dto.storeOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class StoreOrderTrendResponse {
    private LocalDate date;
    private int totalQuantity;
    private long totalAmount;
    private int totalOrders;
}
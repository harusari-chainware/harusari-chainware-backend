package com.harusari.chainware.statistics.query.dto.menuSales;

import lombok.Getter;

@Getter
public class MenuSalesResponse {
    private Long menuId;
    private String menuName;
    private int totalQuantity;
    private long totalAmount;
    private double salesRatio;  // 비중 %
}

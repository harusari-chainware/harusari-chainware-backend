package com.harusari.chainware.takeback.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderInfo {
    private String orderCode;
    private int productCount;
    private int totalQuantity;
    private long totalPrice;
}
package com.harusari.chainware.delivery.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeliveryOrderInfo {
    private String orderCode;
    private Integer productCount;
    private Integer totalQuantity;
    private Long totalPrice;
    private LocalDateTime createdAt;
}

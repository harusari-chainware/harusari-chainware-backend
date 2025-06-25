package com.harusari.chainware.order.query.dto.response;

import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderBasicInfo {
    private String orderCode;
    private OrderStatus orderStatus;
    private String deliveryCode;
    private int totalProductCount;
    private long totalPrice;
    private LocalDate deliveryDueDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

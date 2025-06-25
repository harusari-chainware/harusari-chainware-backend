package com.harusari.chainware.order.query.dto.response;

import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import lombok.Getter;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderBasicInfo {
    private String orderCode;
    private OrderStatus orderStatus;
    private String deliveryCode;           // 배송 정보가 있다면
    private int totalProductCount;
    private long totalPrice;
    private LocalDate deliveryDueDate;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

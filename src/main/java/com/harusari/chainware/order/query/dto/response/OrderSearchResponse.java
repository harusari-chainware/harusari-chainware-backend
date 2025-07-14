package com.harusari.chainware.order.query.dto.response;

import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderSearchResponse {
    private Long orderId;
    private String orderCode;
    private String franchiseName;
    private int productCount;
    private long totalPrice;
    private String deliveryCode;
    private LocalDateTime createdAt; // 주문 등록된 날
    private LocalDate deliveryDueDate; // 납기일
    private LocalDateTime deliveryCompletedAt; // 배송 완료일
    private OrderStatus orderStatus; // 주문 상태
}


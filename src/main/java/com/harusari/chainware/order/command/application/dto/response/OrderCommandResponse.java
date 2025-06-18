package com.harusari.chainware.order.command.application.dto.response;

import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderCommandResponse {
    private Long orderId;
    private Long franchiseId;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
}


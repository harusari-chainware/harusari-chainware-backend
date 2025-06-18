package com.harusari.chainware.order.command.application.dto.response;

import com.harusari.chainware.order.command.domain.aggregate.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCommandResponse {
    private Long orderId;
    private Long franchiseId;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
}


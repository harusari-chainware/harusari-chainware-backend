package com.harusari.chainware.order.command.application.dto.response;

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
    private Long storeId;
    private String orderStatus;
    private LocalDateTime orderedAt;
}


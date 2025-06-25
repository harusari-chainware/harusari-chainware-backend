package com.harusari.chainware.order.query.dto.response;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeliveryHistoryInfo {
    private LocalDateTime deliveredAt;
    private String handlerName;
    private DeliveryStatus deliveryStatus;
}

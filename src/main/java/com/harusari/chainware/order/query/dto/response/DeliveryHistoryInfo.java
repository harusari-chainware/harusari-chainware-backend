package com.harusari.chainware.order.query.dto.response;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import lombok.Getter;
import lombok.Builder;

import java.time.LocalDateTime;

@Getter
@Builder
public class DeliveryHistoryInfo {
    private LocalDateTime deliveredAt;
    private String handlerName;
    private DeliveryStatus deliveryStatus;
}

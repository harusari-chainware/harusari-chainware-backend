package com.harusari.chainware.delivery.query.dto.response;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeliveryDetailInfo {
    private String trackingNumber;
    private String carrier;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime startedAt;
    private LocalDateTime deliveredAt;
}

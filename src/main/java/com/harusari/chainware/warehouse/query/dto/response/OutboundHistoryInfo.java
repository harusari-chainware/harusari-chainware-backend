package com.harusari.chainware.warehouse.query.dto.response;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OutboundHistoryInfo {
    private String trackingNumber;
    private String carrier;
    private LocalDateTime createdAt;
    private LocalDateTime startedAt;
    private LocalDateTime deliveredAt;
    private DeliveryStatus deliveryStatus;
    private String franchiseName;
}
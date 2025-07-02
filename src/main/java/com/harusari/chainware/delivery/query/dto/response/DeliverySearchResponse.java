package com.harusari.chainware.delivery.query.dto.response;

import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DeliverySearchResponse {
    private Long deliveryId;
    private Long orderId;
    private Long takeBackId;
    private String trackingNumber;
    private String warehouseName;
    private String franchiseName;
    private String carrier;
    private String orderCode;
    private DeliveryStatus deliveryStatus;
    private LocalDateTime startedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
}

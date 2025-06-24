package com.harusari.chainware.delivery.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryStartRequest {
    private String trackingNumber;
    private String carrier;
}
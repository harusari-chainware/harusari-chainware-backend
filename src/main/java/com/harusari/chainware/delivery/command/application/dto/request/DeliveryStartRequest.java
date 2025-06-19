package com.harusari.chainware.delivery.command.application.dto.request;

import lombok.Getter;

@Getter
public class DeliveryStartRequest {
    private String trackingNumber;
    private String carrier;
}
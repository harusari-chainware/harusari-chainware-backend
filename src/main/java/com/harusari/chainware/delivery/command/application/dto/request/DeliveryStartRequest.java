package com.harusari.chainware.delivery.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class DeliveryStartRequest {

    private String trackingNumber;
    private String carrier;
    private List<DeliveryStartItem> products;

}
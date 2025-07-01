package com.harusari.chainware.delivery.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DeliveryStartItem {
    private Long orderDetailId;
    private Long takeBackDetailId;
    private LocalDate expirationDate;
}
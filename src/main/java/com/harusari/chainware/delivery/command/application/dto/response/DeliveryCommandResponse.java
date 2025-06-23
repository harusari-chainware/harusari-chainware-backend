package com.harusari.chainware.delivery.command.application.dto.response;


import com.harusari.chainware.delivery.command.domain.aggregate.DeliveryStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryCommandResponse {
    private Long deliveryId;
    private DeliveryStatus deliveryStatus;
}

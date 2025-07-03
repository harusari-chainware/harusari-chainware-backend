package com.harusari.chainware.delivery.command.application.service;

import com.harusari.chainware.delivery.command.application.dto.request.DeliveryStartRequest;
import com.harusari.chainware.delivery.command.application.dto.response.DeliveryCommandResponse;

public interface DeliveryCommandService {

    DeliveryCommandResponse startDelivery(Long deliveryId, DeliveryStartRequest request, Long memberId);

    DeliveryCommandResponse completeDelivery(Long deliveryId, Long memberId);

}
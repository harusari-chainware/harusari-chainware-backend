package com.harusari.chainware.order.command.application.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OrderUpdateRequest {
    private LocalDate deliveryDueDate;
    private List<OrderDetailUpdateRequest> orderDetails;
}
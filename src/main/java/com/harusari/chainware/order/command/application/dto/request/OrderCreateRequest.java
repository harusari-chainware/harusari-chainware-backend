package com.harusari.chainware.order.command.application.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class OrderCreateRequest {
    private LocalDate deliveryDueDate;
    private List<OrderDetailCreateRequest> orderDetails;
}



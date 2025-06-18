package com.harusari.chainware.order.command.application.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
public class OrderCreateRequest {
    private Long franchiseId;
    private Long memberId;
    private LocalDate deliveryDueDate;
    private List<OrderDetailCreateRequest> orderDetails;
}



package com.harusari.chainware.order.command.application.dto.request;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {
    private Long storeId;
    private List<OrderDetailCreateRequest> orderDetails;
}


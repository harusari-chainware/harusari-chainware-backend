package com.harusari.chainware.order.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequest {
    private List<OrderDetailUpdateRequest> orderDetails;
}
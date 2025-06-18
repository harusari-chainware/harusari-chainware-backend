package com.harusari.chainware.order.command.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderUpdateRequest {
    private List<OrderDetailUpdateRequest> orderDetails;
}
package com.harusari.chainware.order.command.application.dto.request;

import lombok.Getter;

@Getter
public class OrderDetailCreateRequest {
    private Long productId;
    private Integer quantity;
}

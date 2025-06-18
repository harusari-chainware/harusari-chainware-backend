package com.harusari.chainware.order.command.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailUpdateRequest {
    private Long productId;
    private Integer quantity;
}